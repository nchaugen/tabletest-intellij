package io.github.nchaugen.tabletest.language

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider.createFormattingModelForPsiFile
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.COLON
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.COMMA
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.LEFT_BRACE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.LEFT_BRACKET
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.LINE_COMMENT
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.PIPE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.RIGHT_BRACE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.RIGHT_BRACKET

class TableTestFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val psiFile = formattingContext.node.psi.containingFile
        val hasErrors = !PsiTreeUtil.collectElementsOfType(psiFile, PsiErrorElement::class.java).isEmpty()

        return createFormattingModelForPsiFile(
            psiFile,
            TableTestBlock(
                node = formattingContext.node,
                wrap = null,
                alignment = null,
                spacingBuilder = if (hasErrors) null else createSpacingBuilder(formattingContext.codeStyleSettings)
            ),
            formattingContext.codeStyleSettings
        )
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val commonSettings: CommonCodeStyleSettings = settings.getCommonSettings(TableTestLanguage)

        return SpacingBuilder(settings, TableTestLanguage)
            .around(PIPE).spaces(1)
            .before(LINE_COMMENT).spaces(0)
            .after(LINE_COMMENT).spaces(0)  // COMMENT token includes original spacing
            .before(COLON).spaces(toSpaces(commonSettings.SPACE_BEFORE_COLON))
            .after(COLON).spaces(toSpaces(commonSettings.SPACE_AFTER_COLON))
            .before(COMMA).spaces(toSpaces(commonSettings.SPACE_BEFORE_COMMA))
            .after(COMMA).spaces(toSpaces(commonSettings.SPACE_AFTER_COMMA))
            .after(LEFT_BRACKET).spaces(toSpaces(commonSettings.SPACE_WITHIN_BRACKETS))
            .before(RIGHT_BRACKET).spaces(toSpaces(commonSettings.SPACE_WITHIN_BRACKETS))
            .after(LEFT_BRACE).spaces(toSpaces(commonSettings.SPACE_WITHIN_BRACES))
            .before(RIGHT_BRACE).spaces(toSpaces(commonSettings.SPACE_WITHIN_BRACES))
    }

    private fun toSpaces(enabled: Boolean): Int = if (enabled) 1 else 0
}
