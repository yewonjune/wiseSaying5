
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("== 명언 앱 ==");
        Scanner sc = new Scanner(System.in);

        ArrayList<WiseSaying> wiseSayings = new ArrayList<>();

        int id = 0;

        while (true) {
            System.out.print("명령) ");
            String command = sc.nextLine();

            if (command.equals("등록")) {
                System.out.print("명언: ");
                String content = sc.nextLine();

                System.out.print("작가: ");
                String author = sc.nextLine();

                id++;

                WiseSaying ws = new WiseSaying(id, content, author);
                wiseSayings.add(ws);

                System.out.println(id + "번 명언이 등록되었습니다.");

            } else if (command.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("-------------------------");

                for (int i = wiseSayings.size() - 1; i >= 0; i--) {
                    WiseSaying ws = wiseSayings.get(i);

                    System.out.println(ws.id + " / " + ws.author + " / " + ws.content);
                }

            } else if (command.equals("종료")) {
                break;
            }
        }

        sc.close();
    }
}
