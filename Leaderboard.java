import java.io.*;
import java.util.HashMap;

public class Leaderboard {

    public int highScore;

    private final HashMap<String, Integer> scores = new HashMap<>();

    public Leaderboard() throws IOException {
        initScores();
        if (scores.size() == 0) {
            this.highScore = 0;
        }
    }

    private void initScores() throws IOException {
        File file = new File("src/leaderboard.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while (line != null) {
            String[] entry = line.split(",");
            String name = entry[0];
            Integer score = Integer.parseInt(entry[1]);
            if (this.scores.containsKey(name)) {
                if (score > this.scores.get(name)) {
                    this.scores.put(name, score);
                }
            } else {
                this.scores.put(name, score);
            }
            line = br.readLine();
        }
        br.close();
    }

    public String getScores() {
        StringBuilder str = new StringBuilder();
        for (String name : this.scores.keySet()) {
            str.append(name).append(" ").append(this.scores.get(name)).append("\n");
        }
        return str.toString();
    }

    public void addScore(String name, Integer score) throws IOException {
        this.scores.put(name, score);
        String file = "src/leaderboard.txt";
        FileWriter fw = new FileWriter(file, true);
        fw.write(name + "," + score + "\n");
        fw.close();
    }
}
