/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ssw.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Vector;
import javax.swing.JPanel;

/**
 *
 * @author justin
 */
public class DamageChart extends JPanel {
    private int GridX = 1;
    private int GridY = 1;
    private Vector charts = new Vector();
    private Vector colors = new Vector();

    public DamageChart() {
        setBackground( Color.WHITE );
    }

/**
 * Sets the grid width and height for this damage chart.
 * X should be one greater than the maximum range the unit can hit at
 * Y should be one greater than the maximum damage the unit can produce at any range.
 * 
 * @param x Grid X Units
 * @param y Grid Y Units
 */
    public void SetGridSize( int x, int y ) {
        // sets the number of units to be used for grid x and y
        GridX = x;
        GridY = y;
        repaint();
    }

/**
 * Adds a Damage At Range chart to the chart.  The DAR chart should consist of
 * an int[], with each array element representing the damage at that range.
 * For instance, chart[9] should return the damage the unit produces at range 9.
 * 
 * @param chart The Damage At Range chart.
 * @param ccolor The color to draw the DAR chart's line in.
 */
    public void AddChart( int[] chart, Color ccolor ) {
        // adds a chart to those needing to be plotted
        charts.add( chart );
        colors.add( ccolor );
    }

/**
 * Simply clears the charts vector.  Should be used when loading a new unit, or
 * when a new weapon is added or a weapon removed.
 */
    public void ClearCharts() {
        charts.clear();
    }

    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );

        DrawGrid( (Graphics2D) g );
        DrawCharts( (Graphics2D) g );
    }

    private void DrawCharts( Graphics2D g ) {
        int dx = getSize().width;
        int dy = getSize().height;
        double offX = (double) dx / (double) GridX;
        double offY = (double) dy / (double) GridY;

        g.setStroke( new BasicStroke( 2 ) );
        for( int i = 0; i < charts.size(); i++ ) {
            int[] chart = (int[]) charts.get( i );
            g.setColor( ((Color) colors.get( i )) );
            for( int j = 1; j < chart.length; j++ ) {
                g.draw( new Line2D.Double( (( j - 1 ) * offX), (dy - (chart[j-1] * offY)), (j * offX), (dy - (chart[j] * offY)) ) );
            }
        }
    }

    private void DrawGrid( Graphics2D g ) {
        // draws the grid onto the component
        int dx = getSize().width;
        int dy = getSize().height;
        double offX = (double) dx / (double) GridX;
        double offY = (double) dy / (double) GridY;
        int linemodulo = GridY / 10;
        if( linemodulo <= 0 ) { linemodulo = 1; }

        g.setColor( Color.LIGHT_GRAY );

        for( int i = 1; i < GridX; i++ ) {
            g.draw( new Line2D.Double( offX * i, 0, offX * i, dy ) );
            if( i % 5 == 0 ) {
                g.setColor( Color.BLACK );
                g.drawString( "" + i, (int) offX * i, dy - 4 );
                g.setColor( Color.LIGHT_GRAY );
            }
        }
        for( int i = 1; i < GridY; i++ ) {
            if( i % linemodulo == 0 ) {
                g.draw( new Line2D.Double( 0, (dy - (offY * i)), dx, (dy - (offY * i)) ) );
                g.setColor( Color.BLACK );
                g.drawString( "" + i, 1, (int) (dy - (offY * i) + 5) );
                g.setColor( Color.LIGHT_GRAY );
            }
        }
    }
}
