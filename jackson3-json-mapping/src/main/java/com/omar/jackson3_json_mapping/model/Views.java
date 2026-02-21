package com.omar.jackson3_json_mapping.model;

/**
 * 👁️ JSON View definitions for controlling serialization of Donut objects.
 *
 * <p>Views are hierarchical: each view extends the previous to include more data.</p>
 *
 * <p>This allows selective exposure of fields depending on the API consumer:</p>
 * <ul>
 *   <li>📄 Summary: minimal info for listings</li>
 *   <li>🌐 Public: info for general API consumers</li>
 *   <li>🔒 Internal: sensitive data for internal services</li>
 *   <li>🛠️ Admin: complete data for administrative operations</li>
 * </ul>
 */
public class Views {

    /**
     * Summary view: Minimal information for quick listings
     * Includes: type, price
     */
    public interface Summary {}

    /**
     * Public view: Information suitable for public API consumers
     * Includes: Summary + glaze, toppings, isVegan
     */
    public interface Public extends Summary {}

    /**
     * Internal view: Additional details for internal use
     * Includes: Public + calories, bakedAt
     */
    public interface Internal extends Public {}

    /**
     * Admin view: Complete information for administrative purposes
     * Includes: All fields (no restrictions)
     */
    public interface Admin extends Internal {}
}