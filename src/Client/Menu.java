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
                System.out.println("\033[1m \033[38;5;79m " +
                        " _    _    _    _    _    _    _  \n" +
                        "  / \\  / \\  / \\  / \\  / \\  / \\  / \\ \n" +
                        " ( S )( D )( C )( L )( O )( U )( D )\n" +
                        "  \\_/  \\_/  \\_/  \\_/  \\_/  \\_/  \\_/  \033[0m \033[0m");
                System.out.println("\033[1m\033[38;5;79m- - - - - - - - - - - - - - - - - - -\033[0m \033[0m  \n" +
                        "  1 - Iniciar Sessao         \n" +
                        "  2 - Registar utilizador    \n" +
                        "  0 - Sair                   \n" +
                        "\033[1m\033[38;5;79m- - - - - - - - - - - - - - - - - - -\033[0m \033[0m \n" +
                        " \033[38;5;79m> Opção: \033[0m                     \n");
                break;
            case 1:
                System.out.print("\033[1m\033[38;5;79m- - - - - - - - - - - - - - - - - - -\033[0m \033[0m  \n" +
                        "  1 - Upload de Música        \n" +
                        "  2 - Download de Música      \n" +
                        "  3 - Procurar Música          \n" +
                        "  4 - Biblioteca               \n" +
                        "  0 - Terminar Sessão                  \n" +
                        "\033[1m\033[38;5;79m- - - - - - - - - - - - - - - - - - -\033[0m \033[0m \n" +
                        "\033[38;5;79m> Opção: \033[0m \033[0m                      \n");
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
            System.out.println("\033[1m\033[48;5;79m> Opção Inválida, tente de novo:\033[0m\033[0m");
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
                System.out.println("\033[1m\033[48;5;79m> Opção Inválida, tente de novo:\033[0m\033[0m");
                op = readOption();
            }
        }
        if(opt == 1)
        {
            if (op < 0 || op > 4 )
            {
                System.out.println("\033[1m\033[48;5;79m> Opção Inválida, tente de novo:\033[0m\033[0m");
                op = readOption();
            }
        }
        return op;
    }
}