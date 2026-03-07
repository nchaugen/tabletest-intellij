package io.github.nchaugen.tabletest.language

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiAnnotationMemberValue
import com.intellij.psi.PsiArrayInitializerMemberValue
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiNameValuePair
import com.intellij.psi.util.PsiTreeUtil

/**
 * Injects TableTest into Java @TableTest annotation values that are not handled by XML rules.
 *
 * Behaviour:
 * - Java single-line string value: inject into that host.
 * - Java String[] array value: inject one virtual TableTest file across all string elements.
 * - Mixed arrays with unsupported entries: skip injection.
 */
class TableTestJavaArrayValueInjector : MultiHostInjector {

    override fun elementsToInjectIn(): List<Class<out PsiElement>> =
        listOf(PsiLiteralExpression::class.java)

    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        val literal: PsiLiteralExpression = context as? PsiLiteralExpression ?: return
        val host: PsiLanguageInjectionHost = literal as? PsiLanguageInjectionHost ?: return
        if (literal.value !is String) return

        val valuePair: PsiNameValuePair = enclosingValuePair(literal) ?: return
        if (!isValueParameter(valuePair)) return

        val annotation: PsiAnnotation = PsiTreeUtil.getParentOfType(valuePair, PsiAnnotation::class.java, true) ?: return
        if (!isSupportedTableTestAnnotation(annotation)) return

        when (val annotationValue: PsiAnnotationMemberValue = valuePair.value ?: return) {
            is PsiArrayInitializerMemberValue -> injectArrayValue(registrar, host, annotationValue)
            is PsiExpression -> {
                if (annotationValue == literal) {
                    injectSingleLineValue(registrar, host)
                }
            }
            else -> return
        }
    }

    private fun injectArrayValue(
        registrar: MultiHostRegistrar,
        currentHost: PsiLanguageInjectionHost,
        arrayValue: PsiArrayInitializerMemberValue
    ) {
        val hosts: List<PsiLanguageInjectionHost> = arrayValue.initializers
            .mapNotNull { asJavaStringHost(it) }
        if (hosts.isEmpty()) return
        if (hosts.size != arrayValue.initializers.size) return
        if (currentHost !in hosts) return

        registrar.startInjecting(TableTestLanguage)
        hosts.forEach { host ->
            registrar.addPlace(null, "\n", host, ElementManipulators.getValueTextRange(host))
        }
        registrar.doneInjecting()
    }

    private fun injectSingleLineValue(registrar: MultiHostRegistrar, host: PsiLanguageInjectionHost) {
        val value: Any = (host as PsiLiteralExpression).value ?: return
        if (value !is String) return
        if (value.contains('\n')) return

        val parentPair: PsiNameValuePair = host.parent as? PsiNameValuePair ?: return
        if (parentPair.value !== host) return
        if (parentPair.name != null && parentPair.name != PsiAnnotation.DEFAULT_REFERENCED_METHOD_NAME) return

        registrar.startInjecting(TableTestLanguage)
        registrar.addPlace(null, null, host, ElementManipulators.getValueTextRange(host))
        registrar.doneInjecting()
    }

    private fun isSupportedTableTestAnnotation(annotation: PsiAnnotation): Boolean {
        val qualifiedName: String = annotation.qualifiedName ?: return false
        return qualifiedName in SUPPORTED_ANNOTATION_FQNS
    }

    private fun isValueParameter(valuePair: PsiNameValuePair): Boolean =
        valuePair.name == null || valuePair.name == PsiAnnotation.DEFAULT_REFERENCED_METHOD_NAME

    private fun enclosingValuePair(literal: PsiLiteralExpression): PsiNameValuePair? {
        val directParent: PsiElement = literal.parent ?: return null
        if (directParent is PsiNameValuePair) return directParent

        val arrayValue: PsiArrayInitializerMemberValue = directParent as? PsiArrayInitializerMemberValue ?: return null
        val pair: PsiElement = arrayValue.parent ?: return null
        return pair as? PsiNameValuePair
    }

    private fun asJavaStringHost(value: PsiAnnotationMemberValue): PsiLanguageInjectionHost? {
        val literal: PsiLiteralExpression = value as? PsiLiteralExpression ?: return null
        if (literal.value !is String) return null
        return literal as? PsiLanguageInjectionHost
    }

    companion object {
        private val SUPPORTED_ANNOTATION_FQNS: Set<String> = setOf(
            "io.github.nchaugen.tabletest.junit.TableTest",
            "org.tabletest.junit.TableTest",
        )
    }
}
