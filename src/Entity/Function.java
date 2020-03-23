package Entity;

public class Function {
    private final String nome;
    private final Executavel func;

    public Function(String nome, Executavel func) {
        this.nome = nome;
        this.func = func;
    }

    public String getNome() {
        return nome;
    }

    public Executavel getFunc() {
        return func;
    }

    
}
