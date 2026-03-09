package io.github.nchaugen.tabletest.language

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType

class TableTestCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage() = TableTestLanguage

    override fun getCodeSample(settingsType: SettingsType): String =
        """
        input|list|map|set|expected?
        x|[a,b]|[k:v,foo:bar]|{1,2}|y
        """.trimIndent() + "\n"

    override fun getFileExt(): String = TableTestFileType.defaultExtension

    override fun createFileFromText(project: Project, text: String): PsiFile =
        PsiFileFactory.getInstance(project).createFileFromText(
            "CodeStyleSample.${TableTestFileType.defaultExtension}",
            TableTestFileType,
            text
        )

    override fun createConfigurable(
        baseSettings: CodeStyleSettings,
        modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable =
        object : CodeStyleAbstractConfigurable(
            baseSettings,
            modelSettings,
            configurableDisplayName
        ) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel =
                object : TabbedLanguageCodeStylePanel(TableTestLanguage, currentSettings, settings) {
                    override fun initTabs(settings: CodeStyleSettings) {
                        addSpacesTab(settings)
                    }
                }
        }

    override fun customizeSettings(
        consumer: CodeStyleSettingsCustomizable,
        settingsType: SettingsType
    ) {
        if (settingsType == SettingsType.SPACING_SETTINGS) {
            consumer.showStandardOptions(
                "SPACE_BEFORE_COMMA",
                "SPACE_AFTER_COMMA",
                "SPACE_BEFORE_COLON",
                "SPACE_AFTER_COLON",
                "SPACE_WITHIN_BRACKETS",
                "SPACE_WITHIN_BRACES"
            )
        }
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getDefaultCommonSettings(): CommonCodeStyleSettings {
        val settings = CommonCodeStyleSettings(TableTestLanguage)
        settings.SPACE_BEFORE_COMMA = false
        settings.SPACE_AFTER_COMMA = true
        settings.SPACE_BEFORE_COLON = false
        settings.SPACE_AFTER_COLON = true
        settings.SPACE_WITHIN_BRACKETS = false
        settings.SPACE_WITHIN_BRACES = false
        return settings
    }
}
