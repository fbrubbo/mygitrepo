package br.com.chai.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * A collection of utility methods for Swing.<br>
 * This class reduce the duplicate code for simple funtionalities.
 *
 * @author Fernando Barden Rubbo
 */
public final class SwingUtil {

    /**
     * Default constructor. The visibility was reduced to avoid that anyone
     * instantiate this class
     */
    private SwingUtil() {
        // SwingUtil is just a utility for static methods
    }

    /**
     * Sets some default functionalities for the application.
     *
     * @see #setDefaultLookAndFeelDecorated()
     * @see #setDefaultUncaughtExceptionHandler()
     */
    public static void initDefaults(final JFrame frame) {
        setDefaultLookAndFeelDecorated();
        setDefaultUncaughtExceptionHandler();
		setMinimumAndMaximumSize(frame);
		SwingUtil.centralize(frame);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	public static void setMinimumAndMaximumSize(final JFrame frame) {
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setMinimumSize(new Dimension(800, 600));
		frame.setSize(getInitialSize(screenSize));
		frame.setMaximumSize(screenSize);
	}

	/**
	 * Obtem o tamanho inicial da tela. Se a resolução atual for maior ou igual a 1024X78,<br>
	 * a resolução inicial será 1024X768 Senão será 800X600
	 *
	 * @param screenSize tamanho da tela
	 */
	private static Dimension getInitialSize(final Dimension screenSize){
		if((screenSize.getWidth() >= 1024) && (screenSize.getHeight() >= 768)){
			return new Dimension(1024, 768);
		}else{
			return new Dimension(800, 600);
		}
	}

    /**
     * Sets the {@link JFrame#setDefaultLookAndFeelDecorated(boolean)} for
     * <code>true</code>.
     */
    public static void setDefaultLookAndFeelDecorated() {
        // JFrame.setDefaultLookAndFeelDecorated(true);
        try {
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
    }

    /**
     * Set a default exception handler invoked when a thread abruptly terminates
     * due to an uncaught exception, and no other handler has been defined for
     * that thread.
     *
     * @see ExceptionHandler
     */
    public static void setDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
    }

    /**
     * Sets a exception handler invoked when this thread abruptly terminates due
     * to an uncaught exception.
     *
     * @param parent The parent component used to centralize the error dialog.
     * @see ExceptionHandler
     */
    public static void setUncaughtExceptionHandler(final Component parent) {
		Thread.currentThread().setUncaughtExceptionHandler(new ExceptionHandler(parent));
    }

    /**
     * Centralize the <code>jFrame</code> in the current screen.
     *
     * @param comp the component that will be centralized.
     */
    public static void centralize(final Component comp) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = comp.getWidth();
        int height = comp.getHeight();
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;

        Rectangle bounds = new Rectangle(x, y, width, height);
        comp.setBounds(bounds);
    }

    /**
     * Sets to <code>field</code> the input focus and select the
     * <code>field's text</code>.
     *
     * @param field the text field that will receive the focus.
     */
    public static void requestFocus(final Component field) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
			public void run() {
                field.requestFocusInWindow();
            }
        });
    }

    /**
     * Method used to handler exceptions.<br>
     * It shows an error dialog and log the error print stack trace.
     *
     * @param parent The parent component used to centralize the error dialog.
     * @param ex The exception to be handled.
     */
    public static void handleException(final Component parent, final Throwable ex) {
        ExceptionHandler.handleException(parent, ex);
    }

    /**
     * Shows an information dialog.<br>
     * It should be used when the message represents only an information to be
     * present to user.
     *
     * @param parent The parent component used to centralize the information dialog
     * @param message The information message
     */
    public static void showInformationDialog(final Component parent, final String message) {
        showInformationDialog(parent, message, "Informação");
    }

    /**
     * Shows an information dialog.<br>
     * It should be used when the message represents only an information to be
     * present to user.
     *
     * @param parent The parent component used to centralize the information dialog
     * @param message The information message
     * @param title The dialog title
     */
    public static void showInformationDialog(final Component parent, final String message, final String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a warning dialog.<br>
     * It should be used when the message represents a warning to be present to
     * user.
     *
     * @param parent The parent component used to centralize the warning dialog
     * @param message The warning message
     */
    public static void showWarningDialog(final Component parent, final String message) {
        showWarningDialog(parent, message, "Aviso");
    }

    /**
     * Shows a warning dialog.<br>
     * It should be used when the message represents a warning to be present to
     * user.
     *
     * @param parent The parent component used to centralize the warning dialog
     * @param message The warning message
     * @param title The dialog title
     */
    public static void showWarningDialog(final Component parent, final String message, final String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows a confirm dialog.<br>
     * It should be used when the system needs the user interaction to choose
     * the <code>YES</code> or <code>NO</code> options.
     *
     * @param parent The parent component used to centralize the confirm dialog
     * @param message The question message
     * @return It returns the {@link JOptionPane#YES_OPTION} if the user click in <code>YES</code> button or {@link JOptionPane#NO_OPTION} otherwise.
     */
    public static int showConfirmDialog(final Component parent, final String message) {
        return showConfirmDialog(parent, message, "Confirmar");
    }

    /**
     * Shows a confirm dialog.<br>
     * It should be used when the system needs the user interaction to choose
     * the <code>YES</code> or <code>NO</code> options.
     *
     * @param parent The parent component used to centralize the confirm dialog
     * @param message The question message
     * @param title The dialog title
     * @return It returns the {@link JOptionPane#YES_OPTION} if the user click in <code>YES</code> button or {@link JOptionPane#NO_OPTION} otherwise.
     */
    public static int showConfirmDialog(final Component parent, final String message, final String title) {
        return JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION);

    }

    /**
     * Shows a error dialog.<br>
     * It should be used when the message represents an system error.
     *
     * @param parent The parent component used to centralize the error dialog
     * @param message The error message
     */
    public static void showErrorDialog(final Component parent, final String message) {
        showErrorDialog(parent, message, "Erro");
    }

    /**
     * Shows a error dialog.<br>
     * It should be used when the message represents an system error.
     *
     * @param parent The parent component used to centralize the error dialog
     * @param message The error message
     * @param title The dialog title
     */
	public static void showErrorDialog(final Component parent, final String message, final String title) {
		JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
	}

	public static void showFooterMessage(final String text, final JLabel label) {
		label.setText(text);
	}

	public static void showInformationMessage(final String text, final JLabel label) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				label.setText(text);
			}
		});
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				label.setText(" "); // Um espaço aqui pois estava desconfigurando a tela.
			}
		}, 5000);
	}


}
