package com.google.android.setupcompat.internal;

import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.template.FooterButton;

public class FooterButtonPartnerConfig {
    public final PartnerConfig buttonBackgroundConfig;
    public final PartnerConfig buttonDisableAlphaConfig;
    public final PartnerConfig buttonDisableBackgroundConfig;
    public final PartnerConfig buttonDisableTextColorConfig;
    public final PartnerConfig buttonIconConfig;
    public final PartnerConfig buttonMarginStartConfig;
    public final PartnerConfig buttonMinHeightConfig;
    public final PartnerConfig buttonRadiusConfig;
    public final PartnerConfig buttonRippleColorAlphaConfig;
    public final PartnerConfig buttonTextColorConfig;
    public final PartnerConfig buttonTextSizeConfig;
    public final PartnerConfig buttonTextStyleConfig;
    public final PartnerConfig buttonTextTypeFaceConfig;
    public final int partnerTheme;

    public FooterButtonPartnerConfig(int i, PartnerConfig partnerConfig, PartnerConfig partnerConfig2, PartnerConfig partnerConfig3, PartnerConfig partnerConfig4, PartnerConfig partnerConfig5, PartnerConfig partnerConfig6, PartnerConfig partnerConfig7, PartnerConfig partnerConfig8, PartnerConfig partnerConfig9, PartnerConfig partnerConfig10, PartnerConfig partnerConfig11, PartnerConfig partnerConfig12, PartnerConfig partnerConfig13) {
        this.partnerTheme = i;
        this.buttonTextColorConfig = partnerConfig6;
        this.buttonMarginStartConfig = partnerConfig7;
        this.buttonTextSizeConfig = partnerConfig8;
        this.buttonMinHeightConfig = partnerConfig9;
        this.buttonTextTypeFaceConfig = partnerConfig10;
        this.buttonTextStyleConfig = partnerConfig11;
        this.buttonBackgroundConfig = partnerConfig;
        this.buttonDisableAlphaConfig = partnerConfig2;
        this.buttonDisableBackgroundConfig = partnerConfig3;
        this.buttonDisableTextColorConfig = partnerConfig4;
        this.buttonRadiusConfig = partnerConfig12;
        this.buttonIconConfig = partnerConfig5;
        this.buttonRippleColorAlphaConfig = partnerConfig13;
    }

    public int getPartnerTheme() {
        return this.partnerTheme;
    }

    public PartnerConfig getButtonBackgroundConfig() {
        return this.buttonBackgroundConfig;
    }

    public PartnerConfig getButtonDisableAlphaConfig() {
        return this.buttonDisableAlphaConfig;
    }

    public PartnerConfig getButtonDisableBackgroundConfig() {
        return this.buttonDisableBackgroundConfig;
    }

    public PartnerConfig getButtonDisableTextColorConfig() {
        return this.buttonDisableTextColorConfig;
    }

    public PartnerConfig getButtonIconConfig() {
        return this.buttonIconConfig;
    }

    public PartnerConfig getButtonTextColorConfig() {
        return this.buttonTextColorConfig;
    }

    public PartnerConfig getButtonMarginStartConfig() {
        return this.buttonMarginStartConfig;
    }

    public PartnerConfig getButtonMinHeightConfig() {
        return this.buttonMinHeightConfig;
    }

    public PartnerConfig getButtonTextSizeConfig() {
        return this.buttonTextSizeConfig;
    }

    public PartnerConfig getButtonTextTypeFaceConfig() {
        return this.buttonTextTypeFaceConfig;
    }

    public PartnerConfig getButtonTextStyleConfig() {
        return this.buttonTextStyleConfig;
    }

    public PartnerConfig getButtonRadiusConfig() {
        return this.buttonRadiusConfig;
    }

    public PartnerConfig getButtonRippleColorAlphaConfig() {
        return this.buttonRippleColorAlphaConfig;
    }

    public static class Builder {
        public PartnerConfig buttonBackgroundConfig = null;
        public PartnerConfig buttonDisableAlphaConfig = null;
        public PartnerConfig buttonDisableBackgroundConfig = null;
        public PartnerConfig buttonDisableTextColorConfig = null;
        public PartnerConfig buttonIconConfig = null;
        public PartnerConfig buttonMarginStartConfig = null;
        public PartnerConfig buttonMinHeight = null;
        public PartnerConfig buttonRadiusConfig = null;
        public PartnerConfig buttonRippleColorAlphaConfig = null;
        public PartnerConfig buttonTextColorConfig = null;
        public PartnerConfig buttonTextSizeConfig = null;
        public PartnerConfig buttonTextStyleConfig = null;
        public PartnerConfig buttonTextTypeFaceConfig = null;
        public final FooterButton footerButton;
        public int partnerTheme;

        public Builder(FooterButton footerButton2) {
            this.footerButton = footerButton2;
            if (footerButton2 != null) {
                this.partnerTheme = footerButton2.getTheme();
            }
        }

        public Builder setButtonBackgroundConfig(PartnerConfig partnerConfig) {
            this.buttonBackgroundConfig = partnerConfig;
            return this;
        }

        public Builder setButtonDisableAlphaConfig(PartnerConfig partnerConfig) {
            this.buttonDisableAlphaConfig = partnerConfig;
            return this;
        }

        public Builder setButtonDisableBackgroundConfig(PartnerConfig partnerConfig) {
            this.buttonDisableBackgroundConfig = partnerConfig;
            return this;
        }

        public Builder setButtonDisableTextColorConfig(PartnerConfig partnerConfig) {
            this.buttonDisableTextColorConfig = partnerConfig;
            return this;
        }

        public Builder setButtonIconConfig(PartnerConfig partnerConfig) {
            this.buttonIconConfig = partnerConfig;
            return this;
        }

        public Builder setMarginStartConfig(PartnerConfig partnerConfig) {
            this.buttonMarginStartConfig = partnerConfig;
            return this;
        }

        public Builder setTextColorConfig(PartnerConfig partnerConfig) {
            this.buttonTextColorConfig = partnerConfig;
            return this;
        }

        public Builder setTextSizeConfig(PartnerConfig partnerConfig) {
            this.buttonTextSizeConfig = partnerConfig;
            return this;
        }

        public Builder setButtonMinHeight(PartnerConfig partnerConfig) {
            this.buttonMinHeight = partnerConfig;
            return this;
        }

        public Builder setTextTypeFaceConfig(PartnerConfig partnerConfig) {
            this.buttonTextTypeFaceConfig = partnerConfig;
            return this;
        }

        public Builder setTextStyleConfig(PartnerConfig partnerConfig) {
            this.buttonTextStyleConfig = partnerConfig;
            return this;
        }

        public Builder setButtonRadiusConfig(PartnerConfig partnerConfig) {
            this.buttonRadiusConfig = partnerConfig;
            return this;
        }

        public Builder setButtonRippleColorAlphaConfig(PartnerConfig partnerConfig) {
            this.buttonRippleColorAlphaConfig = partnerConfig;
            return this;
        }

        public Builder setPartnerTheme(int i) {
            this.partnerTheme = i;
            return this;
        }

        public FooterButtonPartnerConfig build() {
            return new FooterButtonPartnerConfig(this.partnerTheme, this.buttonBackgroundConfig, this.buttonDisableAlphaConfig, this.buttonDisableBackgroundConfig, this.buttonDisableTextColorConfig, this.buttonIconConfig, this.buttonTextColorConfig, this.buttonMarginStartConfig, this.buttonTextSizeConfig, this.buttonMinHeight, this.buttonTextTypeFaceConfig, this.buttonTextStyleConfig, this.buttonRadiusConfig, this.buttonRippleColorAlphaConfig);
        }
    }
}
