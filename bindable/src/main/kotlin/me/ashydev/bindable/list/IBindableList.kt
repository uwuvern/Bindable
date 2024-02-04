/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable.list

import me.ashydev.bindable.IBindable
import me.ashydev.bindable.IBindableContainer
import me.ashydev.bindable.IUnbindable
import me.ashydev.bindable.types.ICollectionModify
import me.ashydev.bindable.types.ICopyable
import me.ashydev.bindable.types.IDisableable
import me.ashydev.bindable.types.IHasDefault
import java.io.Serializable

interface IBindableList<T, V : IBindableList<T, V>>
    : MutableList<T>, IDisableable, IUnbindable<V>,
    ICopyable<IBindableList<T, V>>, ICollectionModify<T, V>,
    IBindableContainer<V>, Serializable {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <V, T : IBindableList<V, T>> create(source: T): T {
            val copy: IBindableList<V, T> = source.create()

            if (copy.javaClass != source.javaClass) {
                throw IllegalArgumentException("Attempted to create a bindable copy of ${source.javaClass.simpleName}, but the returned type was ${copy.javaClass.simpleName}"
                        + "Override ${source.javaClass.simpleName}.create() for GetBoundCopy() to return the correct type.")
            }

            copy.bindTo(source);
            return copy as T
        }

        fun <T : IBindableList<Any, T>> loose(source: T): T = create(source)
    }
}