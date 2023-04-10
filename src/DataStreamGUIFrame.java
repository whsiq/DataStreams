import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamGUIFrame extends JFrame {

    JPanel mainPnl;
    JPanel textPnl;
    JPanel searchPnl;
    JPanel optionPnl;

    JTextArea fullTxtTA;
    JTextArea filteredTxtTA;
    JTextField searchTF;

    JScrollPane scroller1;
    JScrollPane scroller2;

    JButton loadBtn;
    JButton searchBtn;
    JButton quitBtn;

    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listFiltered = new ArrayList<>();

    public DataStreamGUIFrame() {
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        createSearchPanel();
        mainPnl.add(searchPnl, BorderLayout.NORTH);

        createTextPanel();
        mainPnl.add(textPnl, BorderLayout.CENTER);

        createOptionPanel();
        mainPnl.add(optionPnl, BorderLayout.SOUTH);

        add(mainPnl);
        setSize(900, 700);
        setTitle("Data Streams");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void createTextPanel() {
        textPnl = new JPanel();
        textPnl.setLayout(new GridLayout(1,2));

        fullTxtTA = new JTextArea();
        fullTxtTA.setEditable(false);
        filteredTxtTA = new JTextArea();
        filteredTxtTA.setEditable(false);

        scroller1 = new JScrollPane(fullTxtTA);
        scroller2 = new JScrollPane(filteredTxtTA);

        textPnl.add(scroller1);
        textPnl.add(scroller2);

    }

    public void createSearchPanel() {
        searchPnl = new JPanel();
        searchPnl.setLayout(new BorderLayout());

        searchBtn = new JButton("Search");
        searchBtn.addActionListener((ActionEvent ae) -> searchFile());
        searchTF = new JTextField();            //put columns in if necessary

        searchPnl.add(searchTF, BorderLayout.CENTER);
        searchPnl.add(searchBtn, BorderLayout.EAST);
    }

    public void createOptionPanel() {
        optionPnl = new JPanel();
        optionPnl.setLayout(new GridLayout(1,2));

        loadBtn = new JButton("Load");
        loadBtn.addActionListener((ActionEvent ae) -> loadFile());
        quitBtn = new JButton("Quit");
        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));

        optionPnl.add(loadBtn);
        optionPnl.add(quitBtn);
    }

    public void loadFile() {

        try {

            JFileChooser chooser = new JFileChooser();
            File selectedFile;

            fullTxtTA.setText("");
            filteredTxtTA.setText("");

            try
            {
                File workingDirectory = new File(System.getProperty("user.dir"));
                chooser.setCurrentDirectory(workingDirectory);

                if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    selectedFile = chooser.getSelectedFile();
                    Stream<String> lines = Files.lines(Paths.get(selectedFile.toURI()));
                    lines.forEach(l -> {
                        fullTxtTA.append(l + "\n");
                        list.add(l);
                    });
                    lines.close();
                }
            }
            catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "File not found");
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void searchFile() {
        filteredTxtTA.setText("");
        if(!fullTxtTA.getText().equals("")) {
            if(!searchTF.getText().equals("")) {
                String lowerSearch = searchTF.getText().toLowerCase();
                listFiltered = (ArrayList<String>) list.stream()
                        .filter(w -> w.toLowerCase()
                        .contains(lowerSearch))
                        .collect(Collectors.toList());
                for(String i : listFiltered) {
                    filteredTxtTA.append(i + "\n\n");
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Please enter a search keyword.");
            }
        }

        else {
            JOptionPane.showMessageDialog(null, "Please load a .txt file.");
        }
    }

}
