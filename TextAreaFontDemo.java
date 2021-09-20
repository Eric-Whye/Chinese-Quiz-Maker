import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TextAreaFontDemo extends JPanel {
    private static JTextArea textArea1 = new JTextArea(1, 15);
    private static JTextArea textArea2 = new JTextArea(1, 15);

    public TextAreaFontDemo(JTextArea textArea) {
        initializeUI(textArea);
    }


    private void initializeUI(JTextArea textArea) {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension((1500/2)-6, 100));

        textArea.setLineWrap(true);
        textArea.setText("The quick brown fox jumps over the lazy dog.");

        // Sets JTextArea font and color.
        Font font = new Font("DengXian (Body Asian)", Font.BOLD, 50);
        textArea.setFont(font);
        textArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(textArea);

        this.add(scrollPane, BorderLayout.CENTER);
    }


    private static void showFrame() {
        JPanel panel1 = new TextAreaFontDemo(textArea1);
        JPanel panel2 = new TextAreaFontDemo(textArea2);
        panel1.setOpaque(true);
        panel2.setOpaque(true);
        textArea2.setForeground(Color.WHITE);

        JFrame frame = new JFrame("Chinese Sheet Quiz");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(250,250,1500,250);
        frame.setVisible(true);
        frame.add(panel1, BorderLayout.WEST);
        frame.add(panel2, BorderLayout.EAST);


        JButton button = new JButton();
        button.setPreferredSize(new Dimension(30, 30));
        button.setText("下一个");

        HashMap<String, String> map = new HashMap<>();
        readFile(map);
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();

        ArrayList<Map.Entry<String, String>> list = new ArrayList<>();
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        Collections.shuffle(list);


        AtomicInteger i = new AtomicInteger(1);
        AtomicInteger index = new AtomicInteger();
        textArea1.setText(list.get(index.get()).getKey());
        textArea2.setText(list.get(index.get()).getValue());
        index.incrementAndGet();

        button.addActionListener(e -> {
            if (i.incrementAndGet() % 2 == 0){
                textArea2.setForeground(Color.BLACK);
            } else{
                textArea2.setForeground(Color.WHITE);
                textArea1.setText(list.get(index.get()).getKey());
                textArea2.setText(list.get(index.get()).getValue());
                index.incrementAndGet();
            }
        });

        frame.add(button, BorderLayout.SOUTH);
        textArea2.validate();
        textArea1.validate();
        panel1.validate();
        panel2.validate();
        frame.validate();

    }

    private static void readFile(Map<String, String> map){
        try {

            String path = "C:\\Users\\Eric\\OneDrive - University College Dublin\\Documents";
            File fp = new File(path + "\\Chinese Practice Sheet.docx");

            double SPACING = 1.4;
            double FONT_SIZE = 14;


            if (!fp.exists()) {
                System.out.println("File isn't there");
            }


            //Reading in the word doc, putting all practice words into stringtokenizer, then mapped the english word with the translation.
            FileInputStream fis = new FileInputStream(fp.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            String fileData = extractor.getText();


            assert fileData != null;
            fileData = fileData.replaceAll("\n", "\t");
            StringTokenizer tokens = new StringTokenizer(fileData, "\t");

            String token1 = null;
            String token2 = null;
            int k = 0;
            while (tokens.hasMoreTokens()) {
                token1 = tokens.nextToken();

                while (token1.trim().equals(""))
                    token1 = tokens.nextToken();
                token2 = tokens.nextToken();
                if (token2.trim().equals(""))
                    token2 = tokens.nextToken();

                map.put(token1, token2);
            }

        }catch (Exception ex) { ex.printStackTrace();}

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TextAreaFontDemo.showFrame();
            }
        });
    }
}