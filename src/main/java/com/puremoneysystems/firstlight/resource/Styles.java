package com.puremoneysystems.firstlight.resource;

import static org.jrebirth.core.resource.Resources.create;

import org.jrebirth.core.resource.style.StyleSheet;
import org.jrebirth.core.resource.style.StyleSheetItem;

/**
 * The Styles interface providing all style sheets.
 */
public interface Styles {

    /** The application main style sheet. */
    StyleSheetItem MAIN = create(new StyleSheet("application"));

}
