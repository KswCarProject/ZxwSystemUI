package com.android.systemui.qs;

import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;

public class QSContainerImplController extends ViewController<QSContainerImpl> {
    public final ConfigurationController mConfigurationController;
    public final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onConfigChanged(Configuration configuration) {
            ((QSContainerImpl) QSContainerImplController.this.mView).updateResources(QSContainerImplController.this.mQsPanelController, QSContainerImplController.this.mQuickStatusBarHeaderController);
        }
    };
    public final QSPanelController mQsPanelController;
    public final QuickStatusBarHeaderController mQuickStatusBarHeaderController;

    public QSContainerImplController(QSContainerImpl qSContainerImpl, QSPanelController qSPanelController, QuickStatusBarHeaderController quickStatusBarHeaderController, ConfigurationController configurationController) {
        super(qSContainerImpl);
        this.mQsPanelController = qSPanelController;
        this.mQuickStatusBarHeaderController = quickStatusBarHeaderController;
        this.mConfigurationController = configurationController;
    }

    public void onInit() {
        this.mQuickStatusBarHeaderController.init();
    }

    public void setListening(boolean z) {
        this.mQuickStatusBarHeaderController.setListening(z);
    }

    public void onViewAttached() {
        ((QSContainerImpl) this.mView).updateResources(this.mQsPanelController, this.mQuickStatusBarHeaderController);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    public void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public QSContainerImpl getView() {
        return (QSContainerImpl) this.mView;
    }
}
