/* Copyright (c) 2021 The Brave Authors. All rights reserved.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.chromium.components.browser_ui.site_settings;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import org.chromium.base.BraveReflectionUtil;
import org.chromium.components.browser_ui.settings.ChromeSwitchPreference;
import org.chromium.components.content_settings.ContentSettingValues;
import org.chromium.components.content_settings.ContentSettingsType;
import org.chromium.components.embedder_support.browser_context.BrowserContextHandle;


public class BraveSingleWebsiteSettings extends SiteSettingsPreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) { }

    public static @Nullable String getPreferenceKey(@ContentSettingsType int type) {
        switch (type) {
            case ContentSettingsType.AUTOPLAY:
                return "autoplay_permission_list";
            default:
                return (String) BraveReflectionUtil.InvokeMethod(
                    SingleWebsiteSettings.class, null, "getPreferenceKey",
                    int.class, type);
        }
    }

    public void setupContentSettingsPreferences() {
        for (@ContentSettingsType int type : SiteSettingsUtil.SETTINGS_ORDER) {
            Preference preference = isActionableContentSettingsEnabled()
                    ? new ChromeSwitchPreference(getStyledContext())
                    : new ListPreference(getStyledContext());
            preference.setKey(getPreferenceKey(type));

            if (type == ContentSettingsType.AUTOPLAY) {
                setUpAutoplayPreference(preference);
            }
        }
        // SingleWebsiteSettings.setupContentSettingsPreferences has its own for loop
        BraveReflectionUtil.InvokeMethod(
                    SingleWebsiteSettings.class, null, "setupContentSettingsPreferences");
    }

    private void setUpAutoplayPreference(Preference preference) {
        BrowserContextHandle browserContextHandle =
                getSiteSettingsDelegate().getBrowserContextHandle();
        @ContentSettingValues
        @Nullable
        Website mSite = (Website) BraveReflectionUtil.getField(SingleWebsiteSettings.class, "mSite", this);
        Integer currentValue =
                mSite.getContentSetting(browserContextHandle, ContentSettingsType.AUTOPLAY);
        // In order to always show the sound permission, set it up with the default value if it
        // doesn't have a current value.
        if (currentValue == null) {
            currentValue = WebsitePreferenceBridge.isCategoryEnabled(
                                   browserContextHandle, ContentSettingsType.AUTOPLAY)
                    ? ContentSettingValues.ALLOW
                    : ContentSettingValues.BLOCK;
        }
        // Not possible to embargo AUTOPLAY.
        setupContentSettingsPreference(preference, currentValue, false /* isEmbargoed */);
    }

    private Context getStyledContext() {
        return getPreferenceManager().getContext();
    }

    // Placeholders
    private static boolean isActionableContentSettingsEnabled() {
        assert (false);
        return false;
    }

    private void setupContentSettingsPreference(Preference preference,
            @ContentSettingValues @Nullable Integer value, boolean isEmbargoed) {
        assert (false);
    }
}
