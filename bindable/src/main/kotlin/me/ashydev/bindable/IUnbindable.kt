/*
 * Copyright (c) 2024 Ashley B (uwuvern)
 *
 * This project is licensed under the MIT license, check the LICENSE file in the root of the project for more information.
 */

package me.ashydev.bindable

/**
 * @See IBindable for the base implementation
 *
 * Holding unbinding data for a bindable
 *
 * @param V the child type of the unbindable
 */
interface IUnbindable<V : IUnbindable<V>> {

    /**
     * Unbinds all bindings & events, used for clearing bindable
     *
     * @return the instance
     */
    fun unbind(): V

    /**
     * Handles the unbinding of events
     *
     * @return the instance
     */
    fun unbindEvents(): V

    /**
     * Unbinds the weak referencing bindings
     *
     * @return the instance
     */
    fun unbindBindings(): V

    /**
     * Unbinds our weak references.
     *
     * @return the instance
     */
    fun unbindWeak(): V

    /**
     * Removes our binding on that bindable
     *
     * @return the instance
     */
    fun unbindFrom(unbindable: V): V

    /**
     * Removes our binding on that bindable
     *
     * @return the instance
     */
    fun unbindWeakFrom(unbindable: V): V
}