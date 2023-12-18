import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class TextAnalysis {
    private static ArrayBlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    private static ArrayBlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    private static ArrayBlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        Thread textGenerationThread = new Thread(TextAnalysis::generateTexts);
        Thread analysisThreadA = new Thread(() -> analyzeTexts('a', queueA));
        Thread analysisThreadB = new Thread(() -> analyzeTexts('b', queueB));
        Thread analysisThreadC = new Thread(() -> analyzeTexts('c', queueC));

        textGenerationThread.start();
        analysisThreadA.start();
        analysisThreadB.start();
        analysisThreadC.start();

        try {
            // Ждем завершения всех потоков
            textGenerationThread.join();
            analysisThreadA.join();
            analysisThreadB.join();
            analysisThreadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void generateTexts() {
        for (int i = 0; i < 10000; i++) {
            String text = generateText("abc", 100000);
            try {
                // Размещаем текст в соответствующую очередь
                for (char c : text.toCharArray()) {
                    switch (c) {
                        case 'a':
                            queueA.put(text);
                            break;
                        case 'b':
                            queueB.put(text);
                            break;
                        case 'c':
                            queueC.put(text);
                            break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void analyzeTexts(char symbol, ArrayBlockingQueue<String> queue) {
        int maxCount = 0;
        String maxText = "";
        try {
            while (true) {
                String text = queue.take();
                int count = 0;
                for (char c : text.toCharArray()) {
                    if (c == symbol) {
                        count++;
                    }
                }
                if (count > maxCount) {
                    maxCount = count;
                    maxText = text;
                    System.out.println("Max count of '" + symbol + "': " + maxCount);
                    System.out.println("Text: " + maxText);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



