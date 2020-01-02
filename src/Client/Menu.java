package Client;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
        this.showMenu();
        in = new Scanner(System.in);


    }

    /**
     * Método que devolve o valor da opção.
     * @return Opção
     */
    public int getOpt(){ return this.opt; }

    /**
     * Método que altera o valor da opção
     * @param n Novo valor da opção
     */
    public void setOpt(int n){
        this.opt = n;
    }


    /**
     * Método que apresenta o menu
     */
    public void showMenu() {
        switch (opt) {
            case 0:
                System.out.println(">> MENU <<        \n" +
                        "> 1 - Iniciar Sessao         \n" +
                        "> 2 - Registar utilizador    \n" +
                        "> 0 - Sair                   \n\n"  +
                        "> Opção:                      ");
                break;

            case 1:
                System.out.println(">> MENU <<        \n" +
                        "> 1 - Upload de música       \n" +
                        "> 2 - Download de música     \n" +
                        "> 3 - Procurar música        \n" +
                        "> 4 - Biblioteca             \n" +
                        "> 0 - Terminar Sessão        \n\n" +
                        "> Opção:                     ");
                break;
        }

    }

    /**
     * Método que lê a opção selecionada do menu.
     *@return Opção
     */
    public int readOption()
    {
        int n;
        try { n = Integer.parseInt(in.nextLine()); } catch (NumberFormatException e) {
            System.out.println("\n> Valor inválido, tente de novo: \n");
            n = -1;
        }
        return n;
    }

    /**
     * Método que lê uma mensagem
     * @return Mensagem lida
     */
    public String readString(String m)
    {
        System.out.println(m);
        return in.nextLine();
    }

    /**
     * Método que devolve a opção inserida, verificando se é valida.
     * @return Opção inserida
     */
    public Integer op() {
        int op = readOption();
        if (opt == 0) {
            if (op < 0 || op > 2)
            {
                System.out.println("> Opção Inválida, tente de novo: ");
                op = readOption();
            }
        }
        if(opt == 1)
        {
            if (op < 0 || op > 4 )
            {
                System.out.println("> Opção Inválida, tente de novo: ");
                op = readOption();
            }
        }
        return op;
    }
}