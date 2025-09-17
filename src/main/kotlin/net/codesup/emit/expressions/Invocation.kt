package net.codesup.emit.expressions

import net.codesup.emit.LocalName
import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.Declaration
import net.codesup.emit.declaration.DeclarationScope
import net.codesup.emit.declaration.NamedDeclaration
import net.codesup.emit.use.SymbolUser
import net.codesup.emit.use.TypeUse
import net.codesup.emit.use.Use

open class Invocation(context: SourceBuilder, val declaration: Declaration) : SingleExpr(context), Use, SymbolUser {
    val args = mutableListOf<Expression>()
    val typeArgs = mutableListOf<TypeUse>()

    fun arg(value: Expression): Invocation {
        args.add(value)
        return this
    }

    fun arg(value: NamedDeclaration): Invocation {
        args.add(Lit(sourceBuilder, value.name))
        return this
    }

    operator fun invoke(vararg expr: Expression): Invocation {
        args.addAll(expr)
        return this
    }

    operator fun invoke(vararg expr: NamedDeclaration): Invocation {
        args.addAll(expr.map { Variable(sourceBuilder, it.name) } )
        return this
    }

    fun typeArg(typeUse: TypeUse): Invocation {
        typeArgs.add(typeUse)
        return this
    }

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.w(declaration.qualifiedName).list(scope, typeArgs, prefix = "<", suffix = ">").w("(").list(scope, args).w(")")
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        c.add(args, typeArgs)
        c.add(declaration)
        declaration.reportUsedSymbols(c)
    }
}
