package Executor;

import Entity.Aviso;
import Entity.ErroIgnorar;
import Entity.Executavel;
import Entity.Function;
import Executor.View.Carregamento;
import Executor.View.View;
import java.util.ArrayList;
import java.util.List;

public class Execution {

    private final String nome;
    private String retorno = "";
    private final Carregamento view = new Carregamento();
    private List<Function> runs = new ArrayList<>();
    private List<Executavel> executaveis = new ArrayList<>();
    private boolean errorBreak = false;

    public Execution(String nome, int numberFunctions) {
        this.nome = nome;
        view.setTitle(nome);

        setCarregamento(numberFunctions);
    }

    public Execution(String nome, List<Function> runs) {
        this.nome = nome;
        view.setTitle(nome);

        this.runs = runs;
        setCarregamento(this.runs.size());
    }

    public Execution(String nome) {
        this.nome = nome;
        view.setTitle(nome);
    }

    public void setExecutaveis(List<Executavel> executaveis) {
        this.executaveis = executaveis;
        setCarregamento(this.executaveis.size());

    }

    public void rodarExecutaveis() {
        String nomeExecAtual = "NÃO IDENTIFICADA";
        for (Executavel executavel : executaveis) {
            try {
                nomeExecAtual = executavel.getNome();
                atualizarVisão(nomeExecAtual);

                executavel.run();
                retorno += nomeExecAtual + ": ok\n";
            } catch(Aviso a){
                retorno += "'" + nomeExecAtual + "': " + a.getMessage() + "\n";
                View.render(a.getMessage());
            } catch(ErroIgnorar e){
                e.printStackTrace();
                retorno += "Erro em '" + nomeExecAtual  +"': " + e.toString() + "\n";
                View.render(e.getMessage(),"error");
                //Não dá break, pois é para continuar neste caso
            } catch (Exception e) {
                e.printStackTrace();
                retorno = "Erro em '" + nomeExecAtual + "': " + e.toString();
                View.render(retorno, "error");
                errorBreak =  true;
                break;//sai das execuções
            } catch (Error e) {
                e.printStackTrace();
                retorno = "Erro em '" + nomeExecAtual + "': " + e.toString();
                View.render(retorno, "error");
                errorBreak = true;
                break;//sai das execuções
            }
        }
    }

    public void rodarRunnables() {
        String nomeExecAtual = "NÃO IDENTIFICADA";
        for (Function run : runs) {
            try {
                nomeExecAtual = run.getNome();
                atualizarVisão(nomeExecAtual);

                run.getFunc().run();
                retorno = "";
            } catch (Exception e) {
                e.printStackTrace();
                retorno = "Erro em '" + nomeExecAtual + "': " + e.toString();
                View.render(retorno, "error");
                errorBreak = true;
                break;//sai das execuções
            } catch (Error e) {
                e.printStackTrace();
                retorno = "Erro em '" + nomeExecAtual + "': " + e.toString();
                View.render(retorno, "error");
                errorBreak = true;
                break;//sai das execuções
            }
        }
    }

    private void setCarregamento(int numberFunctions) {
        Carregamento.barra.setMinimum(0);
        Carregamento.barra.setMaximum(numberFunctions);
    }

    public String getRetorno() {
        return retorno;
    }

    public void setMostrarMensagens(boolean mostrarMensagens) {
        View.setMostrarMensagens(mostrarMensagens);
    }

    public void atualizarVisão(String nameNextFunction) {
        view.setVisible(true);
        Carregamento.texto.setText(nameNextFunction);
        Carregamento.barra.setValue(Carregamento.barra.getValue() + 1);
    }

    public void executar(String returnOfFunction) {
        if (!returnOfFunction.equals("")) {
            retorno = returnOfFunction;
            View.render(returnOfFunction, "error");
            errorBreak = true;
            throw new Error(returnOfFunction);
            //Erro
        }
    }
    
    public void finalizar() {
        if(retorno.equals("")){
            retorno = "Execução '" + nome + "' finalizada!";
        }
        View.render(retorno, "success");
        view.dispose();
    }

}
