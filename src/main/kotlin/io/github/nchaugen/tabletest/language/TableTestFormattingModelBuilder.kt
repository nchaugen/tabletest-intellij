package io.github.nchaugen.tabletest.language

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider.createFormattingModelForPsiFile
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CodeStyleSettings
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.COLON
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.COMMA
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.LEFT_BRACE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.LEFT_BRACKET
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.LINE_COMMENT
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.PIPE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.RIGHT_BRACE
import io.github.nchaugen.tabletest.language.psi.TableTestTypes.RIGHT_BRACKET

class TableTestFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel =
        createFormattingModelForPsiFile(
            formattingContext.node.psi.containingFile,
            TableTestBlock(
                node = formattingContext.node,
                wrap = null,
                alignment = null,
                spacingBuilder = createSpacingBuilder(formattingContext.codeStyleSettings)
            ),
            formattingContext.codeStyleSettings
        )

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder =
        SpacingBuilder(settings, TableTestLanguage)
            .around(PIPE).spaces(1)
            .before(LINE_COMMENT).spaces(0)
            .after(LINE_COMMENT).spaces(1)
            .before(COLON).spaces(0)
            .after(COLON).spaces(1)
            .before(COMMA).spaces(0)
            .after(COMMA).spaces(1)
            .after(LEFT_BRACKET).spaces(0)
            .before(RIGHT_BRACKET).spaces(0)
            .after(LEFT_BRACE).spaces(0)
            .before(RIGHT_BRACE).spaces(0)
}
