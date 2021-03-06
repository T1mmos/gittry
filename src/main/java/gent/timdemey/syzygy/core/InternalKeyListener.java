package gent.timdemey.syzygy.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Listens to key events and uses a {@link KeyMapper} to update the key bitmask.
 * @author Timmos
 */
class InternalKeyListener implements KeyListener {

    private final KeyMapper mapper;

    private volatile long keymask = 0;

    InternalKeyListener (KeyMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Action input = mapper.getInput(e.getKeyCode());
        if (input == null) {
            return;
        }
        keymask |= input.getBitMask();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Action input = mapper.getInput(e.getKeyCode());
        if (input == null) {
            return;
        }
        keymask &= ~input.getBitMask();
    }

    public long getKeyMask() {
        return keymask;
    }
}
