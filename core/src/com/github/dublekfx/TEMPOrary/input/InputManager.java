package com.github.dublekfx.TEMPOrary.input;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;

public class InputManager implements InputProcessor {
	
	protected class Key {
        private String name;
        private int keyCode, pressCount;
        private boolean pressed;
        
        public Key(String name, int keyCode){
            this.name = name;
            this.keyCode = keyCode;
            this.pressed = false;
        }
        public void toggle(boolean toggle){
            if(pressed != toggle){
                pressed = toggle;
            }
            if(pressed){
                pressCount++;
            }
        }
		public String getName() {
			return name;
		}
		public boolean isPressed() {
			return pressed;
		}
		public int getKeyCode() {
			return keyCode;
		}
    }

private ArrayList<Key> keys = new ArrayList<Key>();
    
    public InputManager(){}
    
    public void addKeyMapping(String s, int keyCode){
        keys.add(new Key(s, keyCode));
    }
    
    public ArrayList<Key> getKeys()	{
    	return keys;
    }
    
    public boolean isKeyPressed(String s){
        for(int i = 0; i < keys.size(); i++){
            if(s.equals(keys.get(i).getName())){
                return keys.get(i).isPressed();
            }
        }
        return false;
    }
	
	@Override
	public boolean keyDown(int keycode) {
		for(int i = 0; i < keys.size(); i++){
            if(keycode == keys.get(i).getKeyCode()){
                keys.get(i).toggle(true);
            }
        }
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		for(int i = 0; i < keys.size(); i++){
            if(keycode == keys.get(i).getKeyCode()){
                keys.get(i).toggle(false);
            }
        }
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
