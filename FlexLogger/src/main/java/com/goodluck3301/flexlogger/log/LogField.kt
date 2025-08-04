package com.goodluck3301.flexlogger.log


enum class LogField {
    TIMESTAMP,
    LEVEL,
    TAG,
    THREAD,
    MESSAGE;

    override fun toString(): String = name.lowercase()

    companion object {
        /**
         * A set of all valid LogField enum values.
         * This is used for validation purposes to avoid hardcoding allowed values.
         * Automatically stays up-to-date if new enum entries are added.
         *
         * ⚠️ IMPORTANT:
         * When adding a new enum entry here, make sure to handle it appropriately
         * in all relevant parts of the logging system (e.g., formatting logic).
         * Failure to do so may cause unexpected behavior or missing output.
         */
        val allowedFields = entries
    }

}