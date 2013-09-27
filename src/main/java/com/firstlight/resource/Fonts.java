package com.firstlight.resource;

import org.jrebirth.core.resource.font.FontItem;
import org.jrebirth.core.resource.font.RealFont;

import static org.jrebirth.core.resource.Resources.create;

/**
 * The class <strong>Fonts</strong>.
 * 
 * @author
 */
public interface Fonts {

    /** The splash font. */
    FontItem SPLASH = create(new RealFont(FontNames.DINk, 24));

}
