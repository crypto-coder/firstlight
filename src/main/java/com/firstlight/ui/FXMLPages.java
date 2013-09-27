package com.firstlight.ui;

import com.firstlight.ui.dashboard.DashboardModel;

import org.jrebirth.component.ui.stack.PageEnum;
import org.jrebirth.core.concurrent.JRebirthThread;
import org.jrebirth.core.key.UniqueKey;
import org.jrebirth.core.ui.Model;

/**
 * The class <strong>FXMLPages</strong>.
 * 
 * @author Sébastien Bordes
 */
public enum FXMLPages implements PageEnum {

    /** . */
    DashboardFXML,

    /** . */
    LoadWalletFXML,

    /** . */
    ShowWalletSummaryFXML,

    /** . */
    ShowCurrencySummaryFXML

    ;

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<? extends Model> getModelKey() {
        UniqueKey<? extends Model> modelKey = null;

        switch (this) {

            default:
            case DashboardFXML:
                modelKey = JRebirthThread.getThread().getFacade().getUiFacade().buildKey(DashboardModel.class);
                break;
            case LoadWalletFXML:
                //modelKey = JRebirthThread.getThread().getFacade().getUiFacade().buildKey(StandaloneModel.class);
                break;
            case ShowWalletSummaryFXML:
                //modelKey = JRebirthThread.getThread().getFacade().getUiFacade().buildKey(HybridModel.class);
                break;
            case ShowCurrencySummaryFXML:
                //modelKey = JRebirthThread.getThread().getFacade().getUiFacade().buildKey(IncludedModel.class, new LoremIpsum());
                break;
        }

        return modelKey;
    }

}
