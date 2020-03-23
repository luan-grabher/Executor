package Executor.View;

import javax.swing.JOptionPane;

public class View {

    private static boolean mostrarMensagens = true;

    public static void render(String message) {
        render(message, "");
    }

    public static void render(String message, String typeMessage) {
        if (mostrarMensagens) {
            int numberType;
            switch (typeMessage) {
                case "error":
                    numberType = JOptionPane.ERROR_MESSAGE;
                    break;
                case "question":
                    numberType = JOptionPane.QUESTION_MESSAGE;
                    break;
                default:
                    numberType = JOptionPane.INFORMATION_MESSAGE;
                    break;
            }
            JOptionPane.showMessageDialog(null, message, "MENSAGEM DO SISTEMA:", numberType);
        } else {
            String typeMessageString = typeMessage.equals("") ? "" : "[" + typeMessage.toUpperCase() + "] ";
            System.out.println(typeMessageString + message);
        }
    }

    public static void setMostrarMensagens(boolean mostrarMensagens) {
        View.mostrarMensagens = mostrarMensagens;
    }
    
    public static int chooseOption(String title,String message,String[] options){
        return JOptionPane.showOptionDialog(null, message,title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, options[0]);
    }
}
