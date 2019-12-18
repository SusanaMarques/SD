package Client;

import java.util.Scanner;

public class Menu
{
    /** opção atual do menu **/
    private int opt;
    /** Scanner **/
    private Scanner in;

    /**
     * Construtor da classe Client.Menu sem paramêtros
     */
    public Menu()
    {
        this.opt = 0;
        in = new Scanner(System.in);
    }

    /**
     * Método que evolve o valor da opção.
     * @return       opção
     */
    public int getOpt(){ return this.opt; }

    /**
     * Método que altera o valor da opção
     * @param n         novo valor da opção
     */
    public void setOpt(int n){ this.opt = n; }

    /**
     * Método que apresenta o menu
     */
    public void showMenu() {
        switch (opt) {
            case 0:
                System.out.println(">> MENU <<        \n" +
                        "> 1 - Iniciar Sessao         \n" +
                        "> 2 - Registar utilizador    \n" +
                        "> 0 - Sair                   \n");
                break;

            case 1:
                System.out.println(">> MENU <<        \n" +
                        "> 1 - Upload de música       \n" +
                        "> 2 - Download de música     \n" +
                        "> 3 - Procurar música        \n" +
                        "> 0 - Terminar Sessão        \n");
                break;
        }
    }

    /**
     * Método que lê a opção selecionada do menu.
     *@return opção
     */
    public int readOption()
    {
        int n;
        try { n = Integer.parseInt(in.nextLine()); } catch (NumberFormatException e) { System.out.println("\n Valor inválido\n");n = -1; }
        return n;
    }

    /**
     * Método que lê uma mensagem
     * @return        mensagem lida
     */
    public String readString(String m)
    {
        System.out.println(m);
        return in.nextLine();
    }

    /**
     * Método que devolve a opção inserida, verificando se esta é valida.
     * @return      op
     */
    public int option() {
        int op = readOption();
        if (this.opt == 0) {
            while (op < 0 || op > 2) {
                System.out.println("> Escolha uma opção: ");
                op = readOption();
            }
        }
        return op;
    }

}