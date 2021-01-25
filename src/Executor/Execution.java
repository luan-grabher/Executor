package Executor;

import Entity.Warning;
import Entity.ErrorIgnore;
import Entity.Executavel;
import SimpleView.Loading;
import SimpleView.View;
import fileManager.FileManager;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Execution {

    private final String name;
    private String retorno = "";
    private final Loading viewLoading = new Loading();
    private List<Executavel> executables = new ArrayList<>();
    private boolean errorBreak = false;

    public Execution(String nome) {
        this.name = nome;
        viewLoading.setTitle(nome);
    }

    /**
     * Converte um mapa de Executaveis em uma lista de executaveis
     *
     * @param map Mapa de executaveis com o nome de cada execução
     */
    public void setExecutionMap(Map<String, Executavel> map) {
        List<Executavel> execs = new ArrayList<>();

        for (Map.Entry<String, Executavel> entry : map.entrySet()) {
            String key = entry.getKey();
            Executavel value = entry.getValue();
            value.setName(key);
            execs.add(value);
        }

        setExecutables(execs);
    }

    public void setExecutables(List<Executavel> executaveis) {
        this.executables = executaveis;
        setLoading(this.executables.size());
    }

    public void runExecutables() {

        for (Executavel executavel : executables) {
            String nameMethod = "NÃO IDENTIFICADA";

            try {
                nameMethod = executavel.getName();

                //Update loading
                viewLoading.setVisible(true);
                viewLoading.updateBar(nameMethod, viewLoading.barra.getValue() + 1);

                executavel.run();
                retorno += nameMethod + ": ok\n";
            } catch (Warning a) {
                retorno += "'" + nameMethod + "': " + a.getMessage() + "\n";
                View.render(a.getMessage());
            } catch (ErrorIgnore e) {
                e.printStackTrace();
                retorno += "Erro em '" + nameMethod + "': " + e.toString() + "\n";
                View.render(e.getMessage(), "error");
                //Não dá break, pois é para continuar neste caso
            } catch (Exception e) {
                printError(e, nameMethod);
                break;//sai das execuções
            } catch (Error e) {
                printError(e, nameMethod);
                break;//sai das execuções
            }
        }
    }

    private void printError(Error e, String method) {
        e.printStackTrace();
        retorno = "Erro em '" + method + "': " + e.getMessage();
        View.render(retorno, "error");

        FileManager.save(new File(System.getProperty("user.home")) + "\\Desktop\\JavaError.txt", getStackTrace(e));
        View.render("Arquivo de erro java salvo na área de trabalho 'JavaError.txt'. Use se precisar contatar o programador.", "error");

        errorBreak = true;
    }

    private void printError(Exception e, String method) {
        e.printStackTrace();
        retorno = "Erro em '" + method + "': " + e.getMessage();
        View.render(retorno, "error");

        FileManager.save(new File(System.getProperty("user.home")) + "\\Desktop\\JavaError.txt", getStackTrace(e));
        View.render("Arquivo de erro java salvo na área de trabalho 'JavaError.txt'. Use se precisar contatar o programador.", "error");

        errorBreak = true;
    }

    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return sw.toString();
    }

    private String getStackTrace(Error e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return sw.toString();
    }

    private void setLoading(int numberFunctions) {
        viewLoading.start(name, 0, numberFunctions);
    }

    public String getRetorno() {
        return retorno;
    }

    public void setShowMessages(boolean showMessages) {
        View.setShowMessages(showMessages);
    }

    public boolean hasErrorBreak() {
        return errorBreak;
    }

    public void endExecution() {
        endExecution(true);
    }

    public void endExecution(boolean renderReturn) {
        if (retorno.equals("")) {
            retorno = "Execução '" + name + "' finalizada!";
        }
        if (renderReturn && !hasErrorBreak()) {
            View.render(retorno, "success");
        }
        viewLoading.dispose();
    }

}
