/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

// API class

package org.mozilla.javascript;

/**
 * The class of exceptions raised by the engine as described in
 * ECMA edition 3. See section 15.11.6 in particular.
 */
@SuppressWarnings("dep-ann")
public class EcmaError extends RhinoException
{
    static final long serialVersionUID = -6261226256957286699L;

    private String errorName;
    private String errorMessage;

    /**
     * Create an exception with the specified detail message.
     *
     * Errors internal to the JavaScript engine will simply throw a
     * RuntimeException.
     *
     * @param sourceName the name of the source responsible for the error
     * @param lineNumber the line number of the source
     * @param columnNumber the columnNumber of the source (may be zero if
     *                     unknown)
     * @param lineSource the source of the line containing the error (may be
     *                   null if unknown)
     */
    EcmaError(String errorName, String errorMessage,
              String sourceName, int lineNumber,
              String lineSource, int columnNumber)
    {
        recordErrorOrigin(sourceName, lineNumber, lineSource, columnNumber);
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }

    /**
     * @deprecated EcmaError error instances should not be constructed
     *             explicitly since they are generated by the engine.
     */
    public EcmaError(Scriptable nativeError, String sourceName,
                     int lineNumber, int columnNumber, String lineSource)
    {
        this("InternalError", ScriptRuntime.toString(nativeError),
             sourceName, lineNumber, lineSource, columnNumber);
    }

    @Override
    public String details()
    {
        return errorName+": "+errorMessage;
    }

    /**
     * Gets the name of the error.
     *
     * ECMA edition 3 defines the following
     * errors: EvalError, RangeError, ReferenceError,
     * SyntaxError, TypeError, and URIError. Additional error names
     * may be added in the future.
     *
     * See ECMA edition 3, 15.11.7.9.
     *
     * @return the name of the error.
     */
    public String getName()
    {
        return errorName;
    }

    /**
     * Gets the message corresponding to the error.
     *
     * See ECMA edition 3, 15.11.7.10.
     *
     * @return an implementation-defined string describing the error.
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * @deprecated Use {@link RhinoException#sourceName()} from the super class.
     */
    public String getSourceName()
    {
        return sourceName();
    }

    /**
     * @deprecated Use {@link RhinoException#lineNumber()} from the super class.
     */
    public int getLineNumber()
    {
        return lineNumber();
    }

    /**
     * @deprecated
     * Use {@link RhinoException#columnNumber()} from the super class.
     */
    public int getColumnNumber() {
        return columnNumber();
    }

    /**
     * @deprecated Use {@link RhinoException#lineSource()} from the super class.
     */
    public String getLineSource() {
        return lineSource();
    }

    /**
     * @deprecated
     * Always returns <b>null</b>.
     */
    public Scriptable getErrorObject()
    {
        return null;
    }
}
