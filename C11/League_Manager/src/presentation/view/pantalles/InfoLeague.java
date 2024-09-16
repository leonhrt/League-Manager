package presentation.view.pantalles;

import business.model.TableListener;
import business.model.entities.TeamRanking;
import presentation.controller.AdminOptionController;
import presentation.view.ui_elements.ButtonCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This class shows the information of the league in a tabbed pane.
 */
public class InfoLeague extends JTabbedPane {

    //Constants
    public static final String CARD_INFO_LEAGUE = "CARD_INFO_LEAGUE";
    private String LEAGUE_TABLE = "LEAGUE TABLE";
    private String LEAGUE_STATISTICS = "LEAGUE STATISTICS";
    private final String[] titles = { "RANK", "NAME", "NUM. PLAYERS", "P", "W", "D", "L", "PTS" };

    //Panels
    private JPanel jpTable;
    private JPanel jpStatistics;
    private JTable table;
    private DefaultTableModel tableModel;

    // Globals
    private String[][] data;



    /**
     * Constructor the InfoLeague class
     */
    public InfoLeague() {
        setFont(new Font("Inter", Font.BOLD, 17));
        setBorder(BorderFactory.createEmptyBorder(100,0,40,0));
        setBackground(Color.lightGray);

    }

    /**
     * This method creates the tabbed pane with the league table and the league statistics.
     * @param l ActionListener
     */
    private void statisticsTable(TableListener l) {
        jpTable = new JPanel();
        jpTable.setLayout(new GridLayout(1,1));
        jpTable.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));

        // Create table estadísitiques
        table = new JTable(data, titles);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(50);
        table.setShowVerticalLines(false);

         tableModel = new DefaultTableModel(data, titles) {
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        table.setModel(tableModel);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(70);
        table.getColumnModel().getColumn(4).setPreferredWidth(70);
        table.getColumnModel().getColumn(6).setPreferredWidth(70);
        table.getColumnModel().getColumn(7).setPreferredWidth(80);
        table.setFont(new Font("Inter", Font.BOLD, 17));

        JTableHeader jTableHeader = table.getTableHeader();
        jTableHeader.setBackground(Color.getHSBColor(103,53,66));
        jTableHeader.setForeground(Color.white);
        jTableHeader.setFont(new Font("Inter", Font.BOLD, 17));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 8; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        jpTable.add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        table.setRowSelectionAllowed(true);
        ButtonCellRenderer buttonCellRenderer = new ButtonCellRenderer(l);
        buttonCellRenderer.setOpaque(true);
        buttonCellRenderer.setContentAreaFilled(false);
        buttonCellRenderer.setBorderPainted(false);
        table.getColumnModel().getColumn(1).setCellRenderer(buttonCellRenderer);

    }

    /**
     * This method creates the tabbed pane with the league table and the team ranking.
     * @param info ArrayList with the information of the league
     */
    public void loadRanking(ArrayList<TeamRanking> info){
        data = new String[info.size()][8];
        for (int i = 0; i < info.size(); i++){
                data[i][0] = Integer.toString(i+1);
                data[i][1] = info.get(i).getTeam().getName();
                data[i][2] = Integer.toString(info.get(i).getTeam().getPlayers().length);
                data[i][3] = Integer.toString(info.get(i).getPointsPerMatch().size());
                data[i][4] = Integer.toString(info.get(i).getWonMatches());
                data[i][5] = Integer.toString(info.get(i).getTiedMatches());
                data[i][6] = Integer.toString(info.get(i).getLostMatches());
                data[i][7] = Integer.toString(info.get(i).getPoints());
        }

        tableModel.setRowCount(0);
        for (String[] row : data) {
            tableModel.addRow(row);
        }
        revalidate();
    }

    /**
     * Method that creates the statistics graph
     * @param totalMatches total matches that have been played at the time
     *                     it doesn't have to be the same for all teams only
     *                     the number of the team that plays more matches
     * @param points points of each team in each match played
     * @param nameTeams name of each team and the order of the points must be the same
     */

    public void addStatisticsGraph(int totalMatches, ArrayList<ArrayList<Integer>> points, ArrayList<String> nameTeams) {
        jpStatistics.removeAll();
        if(totalMatches != 0) {
            LineGraphic jpGraphic = new LineGraphic(totalMatches);

            for (int i = 0; i < points.size(); i++) {
                jpGraphic.addPoints(points.get(i), nameTeams.get(i));
            }
            jpStatistics.add(jpGraphic);
            revalidate();
        }

    }

    /**
     * This method registers the controller for the buttons
     * @param l ActionListener
     */
    public void registerController(TableListener l) {

        jpTable = new JPanel();
        jpTable.setBackground(Color.lightGray);
        statisticsTable(l);
        addTab(LEAGUE_TABLE,jpTable);

        jpStatistics = new JPanel();
        jpStatistics.setBackground(Color.lightGray);
        addTab(LEAGUE_STATISTICS,jpStatistics);
    }
}
