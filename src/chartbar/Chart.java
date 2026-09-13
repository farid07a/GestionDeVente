package chartbar;

import chartbar.blankchart.BlankPlotChart;
import chartbar.blankchart.BlankPlotChatRender;
import chartbar.blankchart.SeriesSize;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class Chart extends javax.swing.JPanel {

    private List<ModelLegend> legends = new ArrayList<>();
    private List<ModelChart> model = new ArrayList<>();
    private final int seriesSize = 10;
    private final int seriesSpace = 10;
    private int hoveredIndex = -1;
    private int hoveredSeries = -1;
    private final List<Rectangle> barBounds = new ArrayList<>();
    private Consumer<Object[]> hoverListener;
    private final Animator animator;
    private float animate;

    public Chart() {
        initComponents();
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                animate = fraction;
                repaint();
            }
        };
        animator = new Animator(800, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);

        blankPlotChart.addMouseMotionListener(
                new java.awt.event.MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                int oldIndex = hoveredIndex;
                int oldSeries = hoveredSeries;

                Object[] result
                        = getHoveredBarInfo(
                                e.getX(),
                                e.getY()
                        );

                if (result != null) {

                    hoveredIndex = (int) result[0];
                    hoveredSeries = (int) result[5];

                    if (hoverListener != null) {
                        hoverListener.accept(result);
                    }

                } else {

                    hoveredIndex = -1;
                    hoveredSeries = -1;

                    if (hoverListener != null) {
                        hoverListener.accept(null);
                    }
                }

                if (oldIndex != hoveredIndex
                        || oldSeries != hoveredSeries) {

                    blankPlotChart.repaint();
                }
            }
//            @Override
//            public void mouseMoved(
//                    java.awt.event.MouseEvent e) {
//
//                int oldIndex = hoveredIndex;
//                int oldSeries = hoveredSeries;
//
//                Object[] result
//                        = getHoveredBar(
//                                e.getX(),
//                                e.getY()
//                        );
//
//                if (result != null) {
//
//                    hoveredIndex = (int) result[0];
//                    hoveredSeries = (int) result[1];
//
//                    int index = hoveredIndex;
//
//                    String month
//                            = model.get(index).getLabel();
//
//                    double income
//                            = model.get(index).getValues()[0];
//
//                    double expense
//                            = model.get(index).getValues()[1];
//
//                    if (hoverListener != null) {
//
//                        hoverListener.accept(
//                                new Object[]{
//                                    month,
//                                    income,
//                                    expense
//                                }
//                        );
//                    }
//
//                } else {
//
//                    hoveredIndex = -1;
//                    hoveredSeries = -1;
//
//                    if (hoverListener != null) {
//                        hoverListener.accept(null);
//                    }
//                }
////                if (result != null) {
////
////                    hoveredIndex = (int) result[0];
////
////                    if (hoverListener != null) {
////
////                        ModelChart data = model.get(hoveredIndex);
////
////                        double[] values = data.getValues();
////
////                        hoverListener.accept(new Object[]{
////                            data.getLabel(), // اسم المؤسسة
////                            values[0], // Versement
////                            values[1], // Achat
////                            values[2] // LastCredit
////                        });
////                    }
////
////                } else {
////
////                    hoveredIndex = -1;
////
////                    if (hoverListener != null) {
////                        hoverListener.accept(null);
////                    }
////                }
//
//                if (oldIndex != hoveredIndex
//                        || oldSeries != hoveredSeries) {
//
//                    blankPlotChart.repaint();
//                }
//            }
        }
        );
        blankPlotChart.setBlankPlotChatRender(new BlankPlotChatRender() {
            @Override
            public String getLabelText(int index) {
                return model.get(index).getLabel();
            }

            @Override

            public void renderSeries(
                    BlankPlotChart chart,
                    Graphics2D g2,
                    SeriesSize size,
                    int index) {

                double totalSeriesWidth
                        = (seriesSize * legends.size())
                        + (seriesSpace * (legends.size() - 1));

                double x
                        = (size.getWidth() - totalSeriesWidth) / 2;

                for (int i = 0; i < legends.size(); i++) {

                    ModelLegend legend = legends.get(i);

                    double value
                            = model.get(index).getValues()[i];

                    double seriesValues
                            = chart.getSeriesValuesOf(
                                    value,
                                    size.getHeight()
                            ) * animate;

                    int barX
                            = (int) (size.getX() + x);

                    int barY
                            = (int) (size.getY()
                            + size.getHeight()
                            - seriesValues);

                    int barHeight
                            = (int) seriesValues;

                    // رسم العمود
                    g2.setColor(legend.getColor());

                    g2.fillRect(
                            barX,
                            barY,
                            seriesSize,
                            barHeight
                    );

                    // ==============================
                    // إظهار القيمة عند Hover فقط
                    // ==============================
                    if (hoveredIndex == index
                            && hoveredSeries == i) {

                        String text = String.format(
                                "%,.2f دج",
                                value
                        );

                        FontMetrics fm = g2.getFontMetrics();

                        int textWidth = fm.stringWidth(text);

                        // نخلي الكتابة متمركزة فوق العمود
                        int textX = barX
                                + (seriesSize - textWidth) / 2;

                        int textY = barY - 8;

                        g2.setColor(Color.DARK_GRAY);

                        g2.drawString(
                                text,
                                textX,
                                textY
                        );
                    }
//                    if (hoveredIndex == index
//                            && hoveredSeries == i) {
//
////                        String text
////                                = String.format(
////                                        "%,.2f",
////                                        value
////                                );
////                        String text
////                                = legends.get(i).getName()
////                                + " : "
////                                + String.format("ّ%,.2f DA", value);
//                        String text
//                                = legends.get(i).getName()
//                                + " : "
//                                + String.format("%,.2f دج", value);
//                        FontMetrics fm
//                                = g2.getFontMetrics();
//
//                        int textWidth
//                                = fm.stringWidth(text);
//
//                        int textX
//                                = barX
//                                + (seriesSize - textWidth) / 2;
//
//                        int textY
//                                = barY - 8;
//
//                        g2.setColor(Color.DARK_GRAY);
//
//                        g2.drawString(
//                                text,
//                                textX,
//                                textY
//                        );
//                    }

                    x += seriesSpace + seriesSize;
                }
            }

        });

    }

    private Object[] getHoveredBar(int mouseX, int mouseY) {

        int labelCount = model.size();

        if (labelCount == 0) {
            return null;
        }

        Graphics2D g2
                = (Graphics2D) blankPlotChart.getGraphics();

        if (g2 == null) {
            return null;
        }

        Insets insets
                = blankPlotChart.getInsets();

        double textWidth
                = blankPlotChart.getMaxValuesTextWidth(g2);

        double textHeight
                = blankPlotChart.getLabelTextHeight(g2);

        double spaceText = 5;

        double width
                = blankPlotChart.getWidth()
                - insets.left
                - insets.right
                - textWidth
                - spaceText;

        double height
                = blankPlotChart.getHeight()
                - insets.top
                - insets.bottom
                - textHeight;

        double space
                = width / labelCount;

        double locationX
                = insets.left
                + textWidth
                + spaceText;

        for (int index = 0;
                index < labelCount;
                index++) {

            double barX
                    = locationX
                    + space * index;

            double totalSeriesWidth
                    = (seriesSize * legends.size())
                    + (seriesSpace * (legends.size() - 1));

            double barStartX
                    = barX
                    + (space - totalSeriesWidth) / 2;

            // نفحص كل الأعمدة داخل الشهر
            for (int series = 0;
                    series < legends.size();
                    series++) {

                double x
                        = barStartX
                        + series * (seriesSize + seriesSpace);

                double value
                        = model.get(index)
                                .getValues()[series];

                double seriesValues
                        = blankPlotChart.getSeriesValuesOf(
                                value,
                                height
                        ) * animate;

                double barY
                        = insets.top
                        + height
                        - seriesValues;
                
//                  if (mouseX >= barStartX
//        && mouseX <= barStartX + totalSeriesWidth
//        && mouseY >= insets.top
//        && mouseY <= insets.top + height + textHeight) {      
//                    g2.dispose();
//
//                    return new Object[]{
//                        index,
//                        series
//                    };
//                }
                        if (mouseX >= x
                        && mouseX <= x + seriesSize
                        && mouseY >= barY
                        && mouseY <= insets.top + height) {

                    g2.dispose();

                    return new Object[]{
                        index,
                        series
                    };
                }
                            
            }
        }

        g2.dispose();

        return null;
    }

    private Object[] getHoveredBarInfo(int mouseX, int mouseY) {

        Object[] result = getHoveredBar(mouseX, mouseY);

        if (result == null) {
            return null;
        }

        int index = (int) result[0];
        int series = (int) result[1];

        String entreprise
                = model.get(index).getLabel();

        double[] values
                = model.get(index).getValues();

        return new Object[]{
            index, // 0
            entreprise, // 1
            values[0], // 2 versement
            values[1], // 3 achat
            values[2], // 4 credit
            series, // 5
            values[series] // 6 valeur hovered
        };
    }
//    private Object[] getHoveredBarInfo(int mouseX, int mouseY) {
//
//        Object[] result = getHoveredBar(mouseX, mouseY);
//
//        if (result == null) {
//            return null;
//        }
//
//        int index = (int) result[0];
//        int series = (int) result[1];
//
//        String label
//                = model.get(index).getLabel();
//
//        double value
//                = model.get(index).getValues()[series];
//
//        String legend
//                = legends.get(series).getName();
//
//        return new Object[]{
//            index,
//            label,
//            series,
//            legend,
//            value
//        };
//    }

    public void addLegend(String name, Color color) {
        ModelLegend data = new ModelLegend(name, color);
        legends.add(data);
        panelLegend.add(new LegendItem(data));
        panelLegend.repaint();
        panelLegend.revalidate();
    }

    public void addData(ModelChart data) {
        model.add(data);
        blankPlotChart.setLabelCount(model.size());
        double max = data.getMaxValues();
        if (max > blankPlotChart.getMaxValues()) {
            blankPlotChart.setMaxValues(max);
        }
    }

    public void clear() {
        animate = 0;
        blankPlotChart.setLabelCount(0);
        model.clear();
        repaint();
    }

    public void start() {
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    public void setHoverListener(Consumer<Object[]> hoverListener) {
        this.hoverListener = hoverListener;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelShadow1 = new chartshadow.PanelShadow();
        blankPlotChart = new chartbar.blankchart.BlankPlotChart();
        panelLegend = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        panelShadow1.setShadowColor(new java.awt.Color(153, 0, 0));

        blankPlotChart.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        blankPlotChart.setValuesFormat("#,##0.00");

        panelLegend.setOpaque(false);
        panelLegend.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        javax.swing.GroupLayout panelShadow1Layout = new javax.swing.GroupLayout(panelShadow1);
        panelShadow1.setLayout(panelShadow1Layout);
        panelShadow1Layout.setHorizontalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelLegend, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .addComponent(blankPlotChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelShadow1Layout.setVerticalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(blankPlotChart, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelLegend, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private chartbar.blankchart.BlankPlotChart blankPlotChart;
    private javax.swing.JPanel panelLegend;
    private chartshadow.PanelShadow panelShadow1;
    // End of variables declaration//GEN-END:variables
}
