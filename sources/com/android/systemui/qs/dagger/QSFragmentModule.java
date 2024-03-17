package com.android.systemui.qs.dagger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.qs.FooterActionsView;
import com.android.systemui.qs.QSContainerImpl;
import com.android.systemui.qs.QSFooter;
import com.android.systemui.qs.QSFooterView;
import com.android.systemui.qs.QSFooterViewController;
import com.android.systemui.qs.QSFragment;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QuickQSPanel;
import com.android.systemui.qs.QuickStatusBarHeader;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.util.Utils;

public interface QSFragmentModule {
    static Context provideThemedContext(View view) {
        return view.getContext();
    }

    static LayoutInflater provideThemedLayoutInflater(Context context) {
        return LayoutInflater.from(context);
    }

    static View provideRootView(QSFragment qSFragment) {
        return qSFragment.getView();
    }

    static QSPanel provideQSPanel(View view) {
        return (QSPanel) view.findViewById(R$id.quick_settings_panel);
    }

    static QSContainerImpl providesQSContainerImpl(View view) {
        return (QSContainerImpl) view.findViewById(R$id.quick_settings_container);
    }

    static QuickStatusBarHeader providesQuickStatusBarHeader(View view) {
        return (QuickStatusBarHeader) view.findViewById(R$id.header);
    }

    static QuickQSPanel providesQuickQSPanel(QuickStatusBarHeader quickStatusBarHeader) {
        return (QuickQSPanel) quickStatusBarHeader.findViewById(R$id.quick_qs_panel);
    }

    static BatteryMeterView providesBatteryMeterView(QuickStatusBarHeader quickStatusBarHeader) {
        return (BatteryMeterView) quickStatusBarHeader.findViewById(R$id.batteryRemainingIcon);
    }

    static QSFooterView providesQSFooterView(View view) {
        return (QSFooterView) view.findViewById(R$id.qs_footer);
    }

    static FooterActionsView providesQSFooterActionsView(View view) {
        return (FooterActionsView) view.findViewById(R$id.qs_footer_actions);
    }

    static QSFooter providesQSFooter(QSFooterViewController qSFooterViewController) {
        qSFooterViewController.init();
        return qSFooterViewController;
    }

    static QSCustomizer providesQSCutomizer(View view) {
        return (QSCustomizer) view.findViewById(R$id.qs_customize);
    }

    static View providesQSSecurityFooterView(LayoutInflater layoutInflater, FooterActionsView footerActionsView) {
        return layoutInflater.inflate(R$layout.quick_settings_security_footer, footerActionsView, false);
    }

    static boolean providesQSUsingMediaPlayer(Context context) {
        return Utils.useQsMediaPlayer(context);
    }

    static boolean providesQSUsingCollapsedLandscapeMedia(Context context) {
        return Utils.useCollapsedMediaInLandscape(context.getResources());
    }

    static OngoingPrivacyChip providesPrivacyChip(QuickStatusBarHeader quickStatusBarHeader) {
        return (OngoingPrivacyChip) quickStatusBarHeader.findViewById(R$id.privacy_chip);
    }

    static StatusIconContainer providesStatusIconContainer(QuickStatusBarHeader quickStatusBarHeader) {
        return (StatusIconContainer) quickStatusBarHeader.findViewById(R$id.statusIcons);
    }

    static View providesQSFgsManagerFooterView(LayoutInflater layoutInflater, FooterActionsView footerActionsView) {
        return layoutInflater.inflate(R$layout.fgs_footer, footerActionsView, false);
    }
}
