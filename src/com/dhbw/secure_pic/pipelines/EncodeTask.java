package com.dhbw.secure_pic.pipelines;

import com.dhbw.secure_pic.auxiliary.exceptions.CrypterException;
import com.dhbw.secure_pic.auxiliary.exceptions.InsufficientCapacityException;
import com.dhbw.secure_pic.coder.Coder;
import com.dhbw.secure_pic.crypter.Crypter;
import com.dhbw.secure_pic.data.ContainerImage;
import com.dhbw.secure_pic.data.Information;
import com.dhbw.secure_pic.gui.utility.EncodeFinishedHandler;

import javax.swing.*;
import java.util.concurrent.ExecutionException;


// FIXME comment

/**
 * Background task for encrypting and encoding an information into a container image.
 *
 * @author Thu Giang Tran, Frederik Wolter
 */
public class EncodeTask extends SwingWorker<ContainerImage, Void> {

    // region attributes
    /** Coder for encoding the information into the container image. */
    private final Coder coder;
    /** Crypter for encrypting the information. */
    private final Crypter crypter;
    /** Calling gui class must be a EncodeFinishedHandler to handle when encode finishes */
    private final EncodeFinishedHandler caller;
    /** Information to work with. */
    private Information information;
    // endregion

    public EncodeTask(Coder coder, Crypter crypter, Information information, EncodeFinishedHandler caller) {
        this.coder = coder;
        this.crypter = crypter;
        this.information = information;
        this.caller = caller;
    }

    @Override
    protected ContainerImage doInBackground() throws CrypterException, InsufficientCapacityException {
        // initialize progress property.
        setProgress(0);

        // encrypt information
        this.information = this.crypter.encrypt(this.information,
                progress -> setProgress((int) (progress * 0.5))     // progress 0 - 50
        );

        // encode information
        ContainerImage encodedImage = this.coder.encode(this.information,
                progress -> setProgress((int) (progress * 0.5 + 50))    // progress 50 - 100
        );

        // update progress
        setProgress(100);

        return encodedImage;
        // TODO use setProgress(): https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ProgressBarDemoProject/src/components/ProgressBarDemo.java
    }

    @Override
    protected void done() {
        try {
            ContainerImage image = get();
            this.caller.finishedEncode(image);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        // TODO error handling: https://stackoverflow.com/a/6524300/13777031
    }
}
