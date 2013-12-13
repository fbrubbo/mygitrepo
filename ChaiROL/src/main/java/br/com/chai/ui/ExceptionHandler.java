package br.com.chai.ui;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;


/**
 * The Exception Handler used to handler unexpected exceptions.
 *
 * @author Fernando Barden Rubbo
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {



    /** The parent component used to centrilize a Error Dialog. */
    private Component parent;

    /**
     * Creates a <code>ExceptionHandler</code> without a parent component.
     */
    public ExceptionHandler(){
        // Do nothing. Just a default contructor
    }

    /**
     * Creates a <code>ExceptionHandler</code> with a parent component.
     *
     * @param parentComponent
     *            Used to centrilize the Error Dialog.
     */
    public ExceptionHandler(final Component parentComponent){
        this.parent = parentComponent;
    }

    /**
     * Method invoked when the given thread terminates due to the given uncaught
     * exception.
     * <p>
     * Any exception thrown by this method will be ignored by the Java Virtual
     * Machine.
     *
     * @param t
     *            The thread
     * @param e
     *            The exception
     */
    @Override
	public void uncaughtException(final Thread t, final Throwable e) {
        handleException(parent, e);
    }

    /**
     * A helper method used to handler an exception and show a error dialog with
     * the cause.<br>
     * This method is responsible for logging the error for further analysis.
     *
     * @param parent
     *            A component used to centralized the error dialog.
     * @param e
     *            The exception
     */
    public static void handleException(final Component parent,  final Throwable e) {
    	final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        e.printStackTrace();
        SwingUtil.centralize(new ErrorDialog(parent, result.toString(), e.getMessage()));
    }

}
