package presentation.view.pantalles;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is a JComponent that contains a graphic with the points of the teams in the league.
 */
public class LineGraphic extends JComponent {

    // Globals
    private ArrayList<ArrayList<Point>> lines;
    private int totalMatches;
    private int maxPoints = 3;
    private ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
    private ArrayList<Point> coordinateAxis = new ArrayList<>();
    private ArrayList<String> nameTeams = new ArrayList<>();
    private ArrayList<Integer> pointsMatches = new ArrayList<>();

    // Constants
    private static final int FINAL_Y_AXIS= 300;
    private static final int FINAL_X_AXIS= 550;
    private static final int INITIAL_Y_AXIS = 25;
    private static final int INITIAL_X_AXIS = 50;
    private static final int MIN_POINTS_TO_DISPLAY = 2;

    /**
     * Constructor of the class. It creates the panel.
     * @param TotalMatches the total matches played in the league
     */
    public LineGraphic(int TotalMatches) {
        this.totalMatches = TotalMatches;
        setPreferredSize(new Dimension(900, 600));

        lines = new ArrayList<>();
        coordinateAxis.add(new Point(INITIAL_X_AXIS, FINAL_Y_AXIS));
        coordinateAxis.add(new Point(FINAL_X_AXIS, FINAL_Y_AXIS));
        coordinateAxis.add(new Point(INITIAL_X_AXIS, INITIAL_Y_AXIS));
        setVisible(true);
    }

    /**
     * This method paints the component with the information of the matches.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);

        Random random = new Random(Integer.MAX_VALUE);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setFont(new Font("Calibri", Font.BOLD, 14));

        // Draw coordinate axis
        Point start = coordinateAxis.get(0);
        Point end = coordinateAxis.get(1);
        g2d.drawLine(start.x, start.y, end.x, end.y);
        start = coordinateAxis.get(0);
        end = coordinateAxis.get(2);
        g2d.drawLine(start.x, start.y, end.x, end.y);

        // Draw numbers X axis
        int distanceBetweenXAxis = (FINAL_X_AXIS - INITIAL_X_AXIS) / totalMatches;
        for (int i = 0; i < totalMatches; i++) {
            g.drawString(String.valueOf(i), INITIAL_X_AXIS + (i * distanceBetweenXAxis), FINAL_Y_AXIS + 15);
        }

        // Draw numbers Y axis
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int distanceBetweenYAxis = (FINAL_Y_AXIS - INITIAL_Y_AXIS) / 10;
        int divisions = maxPoints / MIN_POINTS_TO_DISPLAY;
        if (maxPoints / 10 <= 0) {
            divisions = 1;
        } else divisions = maxPoints / 10;
        for (int i = 0; i < 10; i++) {
            String number = String.valueOf(divisions * i);
            g2d.setColor(Color.BLACK);
            g.drawString(number, INITIAL_X_AXIS - (5 + (10 * number.length())), FINAL_Y_AXIS - (i * distanceBetweenYAxis));
            g2d.setColor(Color.LIGHT_GRAY);
            if (i != 0) g.drawLine(INITIAL_X_AXIS, FINAL_Y_AXIS - (i * distanceBetweenYAxis), FINAL_X_AXIS, FINAL_Y_AXIS - (i * distanceBetweenYAxis));
        }

        // Reset stroke
        g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(Color.BLACK);

        // For each group of points
        int distanceBetweenTeamNames = (FINAL_X_AXIS - INITIAL_X_AXIS) / nameTeams.size();
        for (ArrayList<Point> points : lines) {
            if(!points.isEmpty()) {
                // Generate random color
                int red = random.nextInt(256); // Valor aleatori entre 0 y 255 pel component vermell
                int green = random.nextInt(256); // Valor aleatori entre 0 y 255 pel component verd
                int blue = random.nextInt(256); // Valor aleatori entre 0 y 255 pel component blau
                Color colorLine = new Color(red, green, blue);
                g2d.setColor(colorLine); // Posem el color aleatori al stroke
                int punctuation = 0;
                ArrayList<Integer> pointNumbers = temp.get(lines.indexOf(points));
                // Draw lines
                for (int i = 1; i < points.size(); i++) {
                    start = points.get(i - 1);
                    end = points.get(i);
                    punctuation += pointNumbers.get(i - 1);
                    g2d.setColor(Color.black);
                    g2d.drawString(String.valueOf(punctuation), end.x, end.y - 7);
                    g2d.setColor(colorLine);
                    g2d.drawLine(start.x, start.y, end.x, end.y);
                }
            }
            // Draw legend
            g2d.drawLine(INITIAL_X_AXIS + (lines.indexOf(points) * distanceBetweenTeamNames), 365, INITIAL_X_AXIS + (lines.indexOf(points) * distanceBetweenTeamNames) + 30, 365);
            //g2d.setColor(Color.BLACK);
            if (lines.indexOf(points) % 2 == 0) {
                g2d.drawString(nameTeams.get(lines.indexOf(points)), INITIAL_X_AXIS + (lines.indexOf(points) * distanceBetweenTeamNames), 350);
            } else {
                g2d.drawString(nameTeams.get(lines.indexOf(points)), INITIAL_X_AXIS + (lines.indexOf(points) * distanceBetweenTeamNames), 380);
            }
        }
    }

    /**
     * Method that adds a single point to the graphic
     * @param points Number of points to add
     * @param nameTeams Name of the team to add the points
     */
    private void addPoint (int points, String nameTeams) {

        // Buscar si l'equip ja existeix
        for (int i = 0; i < this.nameTeams.size(); i++) {
            // Si existeix, afegir-li els punts
            if (this.nameTeams.get(i).equals(nameTeams)) {
                pointsMatches.set(i, pointsMatches.get(i) + points);
                lines.get(i).add(new Point(INITIAL_X_AXIS + ((lines.get(i).size()) * (FINAL_X_AXIS - INITIAL_X_AXIS) / totalMatches), FINAL_Y_AXIS - (pointsMatches.get(i) * (FINAL_Y_AXIS - INITIAL_Y_AXIS) / maxPoints)));
                // Si els punts superen el màxim, rescal·lar eix Y
                if (pointsMatches.get(i) + points > maxPoints) {
                    int previousMaxPoints = maxPoints;
                    maxPoints = pointsMatches.get(i) + MIN_POINTS_TO_DISPLAY;
                    //TODO: Rescale Y axis and repaint all lines
                    rescaleYAxis(previousMaxPoints);
                }
                repaint();
                return;
            }
        }
        // Si no existeix, afegir-lo
        // Comprovació per saber si ens passem de maxPoints
        if (points > maxPoints) {
            maxPoints = pointsMatches.get(pointsMatches.size() - 1) + MIN_POINTS_TO_DISPLAY;
        }
        this.nameTeams.add(nameTeams);
        lines.add(new ArrayList<>());
        pointsMatches.add(points);

        Point center = new Point(INITIAL_X_AXIS, FINAL_Y_AXIS);
        int xAxis = INITIAL_X_AXIS + ((FINAL_X_AXIS - INITIAL_X_AXIS) / totalMatches);
        int yAxis = FINAL_Y_AXIS - (points * (FINAL_Y_AXIS - INITIAL_Y_AXIS) / maxPoints);

        lines.get(lines.size() - 1).add(center);
        lines.get(lines.size() - 1).add(new Point(xAxis, yAxis));

        repaint();
    }

    /**
     * Rescale Y axis
     * @param previousMaxPoints previous max points
     */
    private void rescaleYAxis(int previousMaxPoints) {
        for (ArrayList<Point> points : lines) {
            for (int i = 1; i < points.size(); i++) {
                Point point = points.get(i);
                int distanceBetweenYAxis = (FINAL_Y_AXIS - INITIAL_Y_AXIS) / previousMaxPoints;
                int punctuation = (FINAL_Y_AXIS - point.y) / distanceBetweenYAxis;
                //System.out.println("Punctuation: " + punctuation + " iteration: " + i);
                point.y = FINAL_Y_AXIS - (punctuation * (FINAL_Y_AXIS - INITIAL_Y_AXIS) / maxPoints);
                points.set(i, point);
            }
        }
    }

    /**
     * Add points to the graph
     * @param points ArrayList of points
     * @param teamName Name of the team
     */
    public void addPoints(ArrayList<Integer> points, String teamName) {
        temp.add(points);
        for (Integer point : points) {
            addPoint(point, teamName);
        }
    }


}
