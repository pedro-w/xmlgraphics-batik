/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.svggen;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.awt.font.*;

import org.w3c.dom.*;

import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.apache.batik.ext.awt.g2d.TransformStackElement;

/**
 * This class performs the task of converting the state of the
 * Java 2D API graphic context into a set of graphic attributes.
 * It also manages a set of SVG definitions referenced by the
 * SVG attributes.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id$
 */
public class SVGGraphicContextConverter {
    private static final String ERROR_CONTEXT_NULL =
        "generatorContext should not be null";

    private static final int GRAPHIC_CONTEXT_CONVERTER_COUNT = 6;

    private SVGTransform transformConverter;
    private SVGPaint paintConverter;
    private SVGBasicStroke strokeConverter;
    private SVGComposite compositeConverter;
    private SVGClip clipConverter;
    private SVGRenderingHints hintsConverter;
    private SVGFont fontConverter;
    private SVGConverter converters[] =
        new SVGConverter[GRAPHIC_CONTEXT_CONVERTER_COUNT];

    public SVGTransform getTransformConverter() { return transformConverter; }
    public SVGPaint getPaintConverter(){ return paintConverter; }
    public SVGBasicStroke getStrokeConverter(){ return strokeConverter; }
    public SVGComposite getCompositeConverter(){ return compositeConverter; }
    public SVGClip getClipConverter(){ return clipConverter; }
    public SVGRenderingHints getHintsConverter(){ return hintsConverter; }
    public SVGFont getFontConverter(){ return fontConverter; }

    /**
     * @param generatorContext the context that will be used to create
     * elements, handle extension and images.
     */
    public SVGGraphicContextConverter(SVGGeneratorContext generatorContext) {
        if (generatorContext == null)
            throw new IllegalArgumentException(ERROR_CONTEXT_NULL);

        transformConverter = new SVGTransform();
        paintConverter = new SVGPaint(generatorContext);
        strokeConverter = new SVGBasicStroke();
        compositeConverter = new SVGComposite(generatorContext);
        clipConverter = new SVGClip(generatorContext);
        hintsConverter = new SVGRenderingHints();
        fontConverter = new SVGFont();

        int i=0;
        converters[i++] = paintConverter;
        converters[i++] = strokeConverter;
        converters[i++] = compositeConverter;
        converters[i++] = clipConverter;
        converters[i++] = hintsConverter;
        converters[i++] = fontConverter;
    }

    /**
     * @return a String containing the transform attribute value
     *         equivalent of the input transform stack.
     */
    public String toSVG(TransformStackElement transformStack[]) {
        return transformConverter.toSVGTransform(transformStack);
    }

    /**
     * @return an object that describes the set of SVG attributes that
     *         represent the equivalent of the input GraphicContext state.
     */
    public SVGGraphicContext toSVG(GraphicContext gc) {
        // no need for synchronized map => use HashMap
        Map groupAttrMap = new HashMap();

        for (int i=0; i<converters.length; i++) {
            SVGDescriptor desc = converters[i].toSVG(gc);
            if (desc != null)
                desc.getAttributeMap(groupAttrMap);
        }

        // the ctor will to the splitting (group/element) job
        return new SVGGraphicContext(groupAttrMap,
                                     gc.getTransformStack());
    }

    /**
     * @return a set of element containing definitions for the attribute
     *         values generated by this converter since its creation.
     */
    public Set getDefinitionSet() {
        Set defSet = new HashSet();
        for(int i=0; i<converters.length; i++)
            defSet.addAll(converters[i].getDefinitionSet());

        return defSet;
    }
}
