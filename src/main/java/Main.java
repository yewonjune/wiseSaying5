import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("==명언 앱==");
        Scanner sc = new Scanner(System.in);

        File folder = new File("db/wiseSaying");
        folder.mkdirs();

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> quotes = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();

        int id = 0;

        File lastIdFile = new File("db/wiseSaying/lastId.txt");

        if(lastIdFile.exists()) {
            Scanner lastIdScanner = new Scanner(lastIdFile);

            if(lastIdScanner.hasNextInt()) {
                id = lastIdScanner.nextInt();
            }

            lastIdScanner.close();
        }

        File[] files = folder.listFiles((dir, name) -> name.matches("\\d+\\.json"));

        if(files != null) {
            for(File file : files) {
                Scanner fileScanner = new Scanner(file);

                fileScanner.nextLine();

                String idLine = fileScanner.nextLine();
                String quoteLine = fileScanner.nextLine();
                String authorLine = fileScanner.nextLine();

                int savedId = Integer.parseInt(
                        idLine.split(":")[1].replace(",", "").trim()
                );

                String savedQuote = quoteLine.split(":", 2)[1]
                        .trim()
                        .replaceAll("^\"|\",?$", "");

                String savedAuthor = authorLine.split(":", 2)[1]
                        .trim()
                        .replaceAll("^\"|\",?$", "");

                ids.add(savedId);
                quotes.add(savedQuote);
                authors.add(savedAuthor);

                if(savedId > id) {
                    id = savedId;
                }

                fileScanner.close();
            }
        }

        while(true) {
            System.out.print("명령) ");
            String command = sc.nextLine();

            if(command.equals("등록")) {
                System.out.print("명언: ");
                String quote = sc.nextLine();

                System.out.print("작가: ");
                String author = sc.nextLine();

                id++;
                ids.add(id);
                quotes.add(quote);
                authors.add(author);

                //JSON파일 만들기
                try {
                FileWriter writer = new FileWriter("db/wiseSaying/"+ id + ".json");

                writer.write("{\n");
                writer.write("\"id\": "+ id + ",\n");
                writer.write("\"content\":\""+ quote + "\",\n");
                writer.write("\"author\":\""+ author + "\"\n");
                writer.write("}");

                writer.close();

                FileWriter lastIdWriter = new FileWriter("db/wiseSaying/lastId.txt");
                lastIdWriter.write(String.valueOf(id));
                lastIdWriter.close();


            }catch(IOException e) {
                e.printStackTrace();
            }

            System.out.println(id + "번 명언이 등록되었습니다.");
        }
            else if(command.equals("목록")) {
            System.out.println("번호 /작가 /명언");
            System.out.println("-----------------------");

            for(int i= ids.size()-1; i>=0; i--)
            {
                System.out.println(ids.get(i) + " / "+ authors.get(i) + " / "+ quotes.get(i));
            }
        }
        else if(command.startsWith("삭제?id=")) {
            int deleteId = Integer.parseInt(command.substring(6));

            boolean found  = false;

            for(int i=0; i<ids.size(); i++) {
                if(ids.get(i) == deleteId) {
                    ids.remove(i);
                    quotes.remove(i);
                    authors.remove(i);

                    File deleteFile = new File("db/wiseSaying/"+ deleteId + ".json");
                    deleteFile.delete();

                    System.out.println(deleteId + "번 명언이 삭제되었습니다.");
                    found = true;
                    break;
                }
            }

            if(!found) {
                System.out.println(deleteId + "번 명언은 존재하지 않습니다.");
            }
        }
        else if(command.startsWith("수정?id=")) {
            int editId  = Integer.parseInt(command.substring(6));

            boolean found = false;

            for(int i=0; i< ids.size(); i++) {
                if(ids.get(i) == editId) {

                    System.out.println("명언(기존) : "+ quotes.get(i));
                    System.out.print("명언 : ");
                    String newQuote = sc.nextLine();

                    System.out.println("작가(기존) : "+ authors.get(i));
                    System.out.print("작가 : ");
                    String newAuthor = sc.nextLine();

                    quotes.set(i, newQuote);
                    authors.set(i, newAuthor);

                    try{
                        FileWriter writer = new FileWriter("db/wiseSaying/"+ editId + ".json");

                        writer.write("{\n");
                        writer.write("\"id\": "+ editId + ",\n");
                        writer.write("\"content\":\""+ newQuote + "\",\n");
                        writer.write("\"author\":\""+ newAuthor + "\"\n");
                        writer.write("}");

                        writer.close();

                    } catch(IOException e) {
                        e.printStackTrace();
                    }

                    found = true;
                    break;
                }
            }

            if(!found) {
                System.out.println(editId + "번 명언은 존재하지 않습니다.");
            }
        }
        else if(command.equals("빌드")) {
                try{
                    FileWriter writer = new FileWriter("db/wiseSaying/data.json");

                    writer.write("[\n");

                    for(int i=0; i<ids.size(); i++) {
                        writer.write("{\n");
                        writer.write("\"id\": " + ids.get(i) + ",\n");
                        writer.write("\"content\":\"" + quotes.get(i) + "\",\n");
                        writer.write("\"author\":\"" + authors.get(i) + "\"\n");
                        writer.write("}");

                        if (i < ids.size() - 1) {
                            writer.write(",");
                        }

                        writer.write("\n");
                    }

                    writer.write("]");
                    writer.close();

                    System.out.println("data.json 파일의 내용이 갱신되었습니다.");

                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        else if(command.equals("종료")) break;
    }
}
}

