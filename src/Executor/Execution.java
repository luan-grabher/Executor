package Executor;

import Entity.Warning;
import Entity.ErrorIgnore;
import Entity.Executavel;
import SimpleView.Loading;
import SimpleView.View;
import java.util.ArrayList;
import java.util.List;

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
                e.printStackTrace();
                retorno = "Erro em '" + nameMethod + "': " + e.toString();
                View.render(retorno, "error");
                errorBreak = true;
                break;//sai das execuções
            } catch (Error e) {
                e.printStackTrace();
                retorno = "Erro em '" + nameMethod + "': " + e.toString();
                View.render(retorno, "error");
                errorBreak = true;
                break;//sai das execuções
            }
        }
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
        if (renderReturn) {
            View.render(retorno, "success");
        }
        viewLoading.dispose();
    }

}
