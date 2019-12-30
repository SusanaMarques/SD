package Client;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Menu
{
    /** Opção atual do menu **/
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
     * @return       Opção
     */
    public int getOpt(){ return this.opt; }

    /**
     * Método que altera o valor da opção
     * @param n         Novo valor da opção
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
     *@return            Opção
     */
    public int readOption()
    {
        int n;

        try { n = Integer.parseInt(in.nextLine()); } catch (NumberFormatException e) {
            System.out.println("\n>Valor inválido\n");
            n = -1;
        }

        return n;
    }

    /**
     * Método que lê uma mensagem
     * @return        Mensagem lida
     */
    public String readString(String m)
    {
        System.out.println(m);
        return in.nextLine();
    }

    public Integer op() {
        System.out.println("Opção: ");
        int opcao = readOption();
        if (opt == 0) {
            while (opcao < 0 || opcao > 2) {
                System.out.println("Opção: ");
                opcao = readOption();
            }
        }
        return opcao;
    }
}