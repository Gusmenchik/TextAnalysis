import java.util.concurrent.ArrayBlockingQueue;

public class TextAnalysis {
    private static ArrayBlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    private static ArrayBlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    private static ArrayBlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    private static final int TOTAL_TEXTS = 10000;

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
            queueA.put(""); // Помещаем пустую строку в очередь, чтобы завершить поток analysisThreadA
            queueB.put(""); // Помещаем пустую строку в очередь, чтобы завершить поток analysisThreadB
            queueC.put(""); // Помещаем пустую строку в очередь, чтобы завершить поток analysisThreadC
            analysisThreadA.join();
            analysisThreadB.join();
            analysisThreadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String generateText(String letters, int length) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt((int) (Math.random() * letters.length())));
        }
        return text.toString();
    }

    public static void generateTexts() {
        for (int i = 0; i < TOTAL_TEXTS; i++) {
            String text = generateText("abc", 100000);
            try {
                // Размещаем текст в соответствующую очередь
                queueA.put(text);
                queueB.put(text);
                queueC.put(text);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void analyzeTexts(char symbol, ArrayBlockingQueue<String> queue) {
        try {
            while (true) {
                String text = queue.take();
                if (text.isEmpty()) {
                    break; // Выход из цикла, если получена пустая строка
                }

                // Подсчет символов
                int count = 0;
                for (char c : text.toCharArray()) {
                    if (c == symbol) {
                        count++;
                    }
                }

                // Вывод результата
                //System.out.println("Text: " + text);
                System.out.println("Count of '" + symbol + "': " + count);
                System.out.println();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




