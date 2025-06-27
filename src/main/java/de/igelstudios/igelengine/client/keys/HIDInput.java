package de.igelstudios.igelengine.client.keys;

import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.client.graphics.Camera;
import de.igelstudios.igelengine.client.gui.GUIManager;
import de.igelstudios.igelengine.common.startup.EngineInitializer;
import de.igelstudios.igelengine.common.startup.KeyInitializer;
import org.lwjgl.glfw.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This class is responsible for every input that is physically given through a HID(Mouse,Keyboard , etc)<br>
 * Keys that are to be used have to be registered in {@link #registerKeys()}<br>
 * Key listeners to be used have to implement {@link KeyListener} with every method using the {@link KeyHandler} annotation and registered using {@link #registerKeyListener(KeyListener)}<br>
 * Key drags to be used have to implement {@link MouseDragListener} with every method using the {@link DragHandler} annotation and registered using
 * {@link #registerDragListener(MouseDragListener)}<br>
 * Mouse move listeners to be used have to implement {@link MouseMoveListener} and registered using {@link #registerMoveListener(MouseMoveListener)}<br>
 * The standard Mouse buttons LMB,MMB and RMB are always registered
 */
public class HIDInput {
    private final Map<String, Map<Method,KeyListener>> listeners;
    private final Map<String, Map<Method,KeyListener>> continousListeners;
    private final Map<String, Map<Method,MouseDragListener>> dragListeners;
    private final Map<String, Map<Method,MouseClickListener>> mouseClickListeners;
    private final Map<Integer,String> defaultKeys;
    private final List<MouseMoveListener> mouseMove;
    private final boolean[] keys = new boolean[GLFW_KEY_LAST];
    private double mouseX, mouseY;
    private final GLFWKeyCallback keyboard;
    private final GLFWCursorPosCallback mousePos;
    private final GLFWMouseButtonCallback mouseKeys;
    private final KeyConfig keyConfig;
    private final Map<Listener,Boolean> activeListeners;
    private static HIDInput instance;

    /**
     * Adds a {@link KeyListener} to be called when the specific key was pressed or released
     * @param listener The listener class to be used<br> Every method being registered has to be annotated with {@link  KeyHandler} annotation
     * @see KeyHandler
     */
    public void registerKeyListener(KeyListener listener){
        activeListeners.put(listener,false);
        for (Method method : listener.getClass().getMethods()) {
            if(!method.isAnnotationPresent(KeyHandler.class))continue;
            if(method.getParameters().length == 1 && method.getParameters()[0].getType().equals(boolean.class)) {
                String name = method.getAnnotation(KeyHandler.class).value();
                boolean contains = false;
                for (String value : defaultKeys.values()) {
                    if (value.equals(name)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) throw new RuntimeException("The key " + name + " has not been registered");
                if (!listeners.containsKey(name)) listeners.put(name, new HashMap<>());
                listeners.get(name).put(method, listener);
            }else if(method.getParameters().length == 0){
                String name = method.getAnnotation(KeyHandler.class).value();
                boolean contains = false;
                for (String value : defaultKeys.values()) {
                    if (value.equals(name)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) throw new RuntimeException("The key " + name + " has not been registered");
                if (!continousListeners.containsKey(name)) continousListeners.put(name, new HashMap<>());
                continousListeners.get(name).put(method, listener);
            }else throw new IllegalArgumentException(method.getName() + " does not have exactly one parameter of type boolean");
        }
    }

    public void registerMouseClickListener(MouseClickListener listener){
        activeListeners.put(listener,false);
        for (Method method : listener.getClass().getMethods()) {
            if(!method.isAnnotationPresent(KeyHandler.class))continue;
            if(method.getParameters().length == 3 && method.getParameters()[0].getType().equals(boolean.class) && method.getParameters()[1].getType().equals(double.class) &&
                    method.getParameters()[2].getType().equals(double.class)) {
                String name = method.getAnnotation(KeyHandler.class).value();
                /*boolean contains = false;
                for (String value : defaultKeys.values()) {
                    if (value.equals(name)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) throw new RuntimeException("The key " + name + " has not been registered");*/
                if (!mouseClickListeners.containsKey(name)) mouseClickListeners.put(name, new HashMap<>());
                mouseClickListeners.get(name).put(method, listener);
            }else throw new IllegalArgumentException(method.getName() + " does not have exactly three parameter of type double,double and boolean");
        }
    }
    
    /**
     * @param listener the listener that will be notified when the mouse has been moved
     */
    public void registerMoveListener(MouseMoveListener listener){
        activeListeners.put(listener,false);
        mouseMove.add(listener);
    }

    public void registerDragListener(MouseDragListener listener){
        activeListeners.put(listener,false);
        for (Method method : listener.getClass().getMethods()) {
            if(!method.isAnnotationPresent(DragHandler.class))continue;
            if(method.getParameters().length != 4 || !method.getParameters()[0].getType().equals(double.class) || !method.getParameters()[1].getType().equals(double.class) ||
                    !method.getParameters()[2].getType().equals(double.class) || !method.getParameters()[3].getType().equals(double.class))
                throw new IllegalArgumentException(method.getName() + " does not have exactly four parameter of type double");
            String name = method.getAnnotation(DragHandler.class).value();
            boolean contains = false;
            for (String value : defaultKeys.values()) {
                if(value.equals(name)){
                    contains = true;
                    break;
                }
            }
            if(!contains)throw new RuntimeException("The key " + name + " has not been registered");
            if(!dragListeners.containsKey(name)) dragListeners.put(name , new HashMap<>());
            dragListeners.get(name).put(method,listener);
        }
    }

    /**
     * Removes a {@link KeyListener} from the callback
     * @param listener The {@link KeyListener} to be used
     * @see #registerKey(int, String) 
     */
    public void removeKeyListener(KeyListener listener){
        activeListeners.remove(listener);
        for (Method method : listener.getClass().getMethods()) {
            if(!method.isAnnotationPresent(KeyHandler.class) || method.getParameters().length != 1 || !method.getParameters()[0].getType().equals(boolean.class))continue;
            String name = method.getAnnotation(KeyHandler.class).value();
            if(!listeners.containsKey(name))continue;
            listeners.get(name).remove(method);
        }
    }


    /**
     * This method is used to define a key which can then be used by {@link KeyListener}
     * @param defaultKey the default Key bind
     * @param name The name under which this key is referenced
     */
    public void registerKey(int defaultKey,String name){
        if(defaultKeys.containsKey(defaultKey) && defaultKeys.get(defaultKey).equals(name))
            throw new IllegalArgumentException("The key: " + ((char) defaultKey) + " already is defined as " + defaultKeys.get(defaultKey));
        defaultKeys.put(defaultKey,name);
    }

    public HIDInput(EngineInitializer initializer){
        instance = this;
        defaultKeys = new HashMap<>();
        listeners = new HashMap<>();
        mouseMove = new ArrayList<>();
        dragListeners = new HashMap<>();
        continousListeners = new HashMap<>();
        mouseClickListeners = new HashMap<>();
        activeListeners = new HashMap<>();
        registerKeys();
        KeyInitializer keyInit = new KeyInitializer();
        initializer.registerKeys(keyInit);
        keyInit.register(this);
        registerListeners();
        keyConfig = new KeyConfig(defaultKeys);
        keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (!keys[key] && action == GLFW_PRESS){
                    if(!GUIManager.handle(mods,key)) {
                        /*if(mouseClickListeners.containsKey(keyConfig.get(key))){
                            mouseClickListeners.get(keyConfig.get(key)).keySet().forEach(method -> {
                                try {
                                    if(activeListeners.get(mouseClickListeners.get(keyConfig.get(key)).get(method))) {
                                        method.invoke(mouseClickListeners.get(keyConfig.get(key)).get(method),mouseX,mouseY ,true);
                                    }
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }*/
                        if (listeners.containsKey(keyConfig.get(key))) {
                            listeners.get(keyConfig.get(key)).keySet().forEach(method -> {
                                try {
                                    if(activeListeners.get(listeners.get(keyConfig.get(key)).get(method))) {
                                        method.invoke(listeners.get(keyConfig.get(key)).get(method), true);
                                    }
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    }
                }else if(keys[key] && action == GLFW_RELEASE) {
                    /*if(mouseClickListeners.containsKey(keyConfig.get(key))){
                        mouseClickListeners.get(keyConfig.get(key)).keySet().forEach(method -> {
                            try {
                                if(activeListeners.get(mouseClickListeners.get(keyConfig.get(key)).get(method))) {
                                    method.invoke(mouseClickListeners.get(keyConfig.get(key)).get(method), mouseX, mouseY, false);
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }*/
                    if(listeners.containsKey(keyConfig.get(key))) {
                        listeners.get(keyConfig.get(key)).keySet().forEach(method -> {
                            try {
                                if(activeListeners.get(listeners.get(keyConfig.get(key)).get(method))) {
                                    method.invoke(listeners.get(keyConfig.get(key)).get(method), false);
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
                keys[key] = (action != GLFW_RELEASE);
            }
        };

        mousePos = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xPos, double yPos) {
                xPos /= Window.getWidth();
                yPos = Window.getHeight() / 2.0f - yPos + (Window.getHeight() / 2.0f);
                yPos /= Window.getHeight();
                double finalXPos = xPos * Camera.getX();
                double finalYPos = yPos * Camera.getY();
                dragListeners.forEach((name, map) -> map.forEach(((method, listener) -> {
                    try {
                        if(keys[keyConfig.get(name)])
                            if(activeListeners.get(listener)) {
                                method.invoke(listener, finalXPos - mouseX, finalYPos - mouseY, finalXPos, finalYPos);
                            }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                })));
                mouseMove.forEach(listeners ->{
                    if(activeListeners.containsKey(listeners)) listeners.mouseMove(finalXPos,finalYPos);
                });
                mouseX = xPos;
                mouseY = yPos;
            }
        };

        mouseKeys = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int key, int action, int mods) {
                if (keys[key]) {
                    if (mouseClickListeners.containsKey(keyConfig.get(key))) {
                        mouseClickListeners.get(keyConfig.get(key)).keySet().forEach(method -> {
                            try {
                                if(activeListeners.get(mouseClickListeners.get(keyConfig.get(key)).get(method))) {
                                    method.invoke(mouseClickListeners.get(keyConfig.get(key)).get(method), false, mouseX * Camera.getX(), mouseY * Camera.getY());
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }else {
                    if(mouseClickListeners.containsKey(keyConfig.get(key))) {
                        Map<Listener,Boolean> listener = new HashMap<>(activeListeners);
                        mouseClickListeners.get(keyConfig.get(key)).keySet().forEach(method -> {
                            try {
                                if(listener.get(mouseClickListeners.get(keyConfig.get(key)).get(method))) {
                                    method.invoke(mouseClickListeners.get(keyConfig.get(key)).get(method), true, mouseX * Camera.getX(), mouseY * Camera.getY());
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
                keys[key] = (action != GLFW_RELEASE);
            }
        };
    }

    public void invokeContinuous(){
        for (String s : continousListeners.keySet()) {
            int key = keyConfig.get(s);
            if(!keys[key])continue;
            continousListeners.get(s).forEach(((method, keyListener) -> {
                try {
                    method.invoke(keyListener);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
    }

    private void registerKeys() {
        //registerKey(GLFW_KEY_D,"right");
        //registerKey(GLFW_KEY_A,"left");
        registerKey(GLFW_MOUSE_BUTTON_1,"LMB");
        registerKey(GLFW_MOUSE_BUTTON_2,"RMB");
        registerKey(GLFW_MOUSE_BUTTON_3,"MMB");
    }

    private void registerListeners() {
        GUIManager.register(this);
    }

    public void close() {
        keyboard.free();
        mousePos.free();
        mouseKeys.free();
    }

    public void registerGLFWListeners(long window){
        org.lwjgl.glfw.GLFW.glfwSetKeyCallback(window,keyboard);
        org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback(window,mouseKeys);
        org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback(window,mousePos);
    }

    public static void activateListener(Listener listener){
        instance.activeListeners.put(listener,true);
    }

    public static void deactivateListener(Listener listener){
        instance.activeListeners.put(listener,false);
    }
}
