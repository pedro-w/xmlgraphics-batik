/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.refimpl.gvt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.AffineTransform;

import java.awt.image.renderable.RenderableImage;
import java.awt.image.RenderedImage;

import org.apache.batik.gvt.RasterImageNode;
import org.apache.batik.gvt.GraphicsNodeRenderContext;

/**
 * An implementation of the <tt>RasterImageNode</tt> interface.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @version $Id$
 */
public class ConcreteRasterImageNode extends AbstractGraphicsNode
        implements RasterImageNode {

    /**
     * The renderable image that represents this image node.
     */
    protected RenderableImage image;
    /**
     * The location of this image node.
     */
    protected Point2D location;
    /**
     * The size of this image node.
     */
    protected Dimension2D size;

    /**
     * Constructs a new empty raster image node.
     */
    public ConcreteRasterImageNode() {}

    //
    // Properties methods
    //

    public void setImage(RenderableImage newImage) {
        RenderableImage oldImage = image;
        this.image = newImage;
        firePropertyChange("image", oldImage, newImage);
    }

    public RenderableImage getImage() {
        return image;
    }

    public void setLocation(Point2D newLocation) {
        Point2D oldLocation = location;
        this.location = newLocation;
        firePropertyChange("location", oldLocation, newLocation);
    }

    public Point2D getLocation() {
        return (Point2D) location.clone();
    }

    public void setSize(Dimension2D newSize) {
        Dimension2D oldSize = size;
        this.size = newSize;
        firePropertyChange("size", oldSize, newSize);
    }

    public Dimension2D getSize() {
        return (Dimension2D) size.clone();
    }

    //
    // Drawing methods
    //

    public boolean hasProgressivePaint() {
        // <!> FIXME : TODO
        return false;
    }

    public void progressivePaint(Graphics2D g2d, GraphicsNodeRenderContext rc) {
        // <!> FIXME : TODO
    }

    public void primitivePaint(Graphics2D g2d, GraphicsNodeRenderContext rc) {
        float x = (float) location.getX();
        float y = (float) location.getY();
        int w = (int) size.getWidth();
        int h = (int) size.getHeight();
        RenderingHints hints = g2d.getRenderingHints();
        RenderedImage scaledImg = image.createScaledRendering(w, h, hints);
        AffineTransform xform = AffineTransform.getTranslateInstance(x, y);
        g2d.drawRenderedImage(scaledImg, xform);
    }

    //
    // Geometric methods
    //

    public Rectangle2D getPrimitiveBounds() {
        float x = (float) location.getX();
        float y = (float) location.getY();
        float w = (float) size.getWidth();
        float h = (float) size.getHeight();
        return new Rectangle2D.Float(x, y, w, h);
    }

    public Shape getOutline() {
        return getBounds();
    }
}
