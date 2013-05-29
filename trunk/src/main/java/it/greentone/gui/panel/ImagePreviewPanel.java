package it.greentone.gui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011 GreenTone Developer Team.<br>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * </code> <br>
 * <br>
 * Pannello di anteprima di un'immagine in un {@link JFileChooser}.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
public class ImagePreviewPanel extends JPanel implements PropertyChangeListener {

    private int width, height;
    private Image image;
    private static final int ACCSIZE = 155;
    private final Color bg;

    /**
     * Pannello di anteprima di un'immagine in un {@link JFileChooser}.
     */
    public ImagePreviewPanel() {
        setPreferredSize(new Dimension(ACCSIZE, -1));
        bg = getBackground();
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();

        // Make sure we are responding to the right event.
        if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            File selection = (File) e.getNewValue();
            if (selection == null)
                return;
            else
                setImage(selection);
        }
    }

    /**
     * Imposta l'immagine nel pannello scalandola.
     * 
     * @param file
     */
    public void setImage(File file) {
        if (file != null) {
            String name = file.getAbsolutePath();
            /*
             * Make reasonably sure we have an image format that AWT can handle so we don't try to draw something silly.
             */
            if ((name != null) && name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg")
                    || name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".png")) {
                ImageIcon icon = new ImageIcon(name);
                image = icon.getImage();
                scaleImage();
                repaint();
            }
        } else {
            image = null;
            repaint();
        }
    }

    /**
     * Imposta l'immagine nel pannello scalandola.
     * 
     * @param bimage
     */
    public void setImage(BufferedImage bimage) {
        if (bimage != null) {
            image = bimage;
            scaleImage();
            repaint();
        }
    }

    private void scaleImage() {
        width = image.getWidth(this);
        height = image.getHeight(this);
        double ratio = 1.0;

        /*
         * Determine how to scale the image. Since the accessory can expand vertically make sure we don't go larger than
         * 150 when scaling vertically.
         */
        if (width >= height) {
            ratio = (double) (ACCSIZE - 5) / width;
            width = ACCSIZE - 5;
            height = (int) (height * ratio);
        } else {
            if (getHeight() > 150) {
                ratio = (double) (ACCSIZE - 5) / height;
                height = ACCSIZE - 5;
                width = (int) (width * ratio);
            } else {
                ratio = (double) getHeight() / height;
                height = getHeight();
                width = (int) (width * ratio);
            }
        }

        image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(bg);

        /*
         * If we don't do this, we will end up with garbage from previous images if they have larger sizes than the one
         * we are currently drawing. Also, it seems that the file list can paint outside of its rectangle, and will
         * cause odd behavior if we don't clear or fill the rectangle for the accessory before drawing. This might be a
         * bug in JFileChooser.
         */
        g.fillRect(0, 0, ACCSIZE, getHeight());
        g.drawImage(image, getWidth() / 2 - width / 2 + 5, getHeight() / 2 - height / 2, this);
    }

    /**
     * Restituisce l'immagine scalata dal pannello.
     * 
     * @return l'immagine scalata dal pannello
     */
    public Image getImage() {
        return image;
    }
}
