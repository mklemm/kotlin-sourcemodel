package net.codesup.emit.expressions

import net.codesup.emit.Generable
import net.codesup.emit.use.SymbolUser

/**
 * @author Mirko Klemm 2025-09-12
 *
 */
interface StatementContext: Generable, SymbolUser {
    val statements: MutableList<Statement>

    fun <S:Statement> addStatement(statement: S):S = statement.also { statements.add(it) }
}
