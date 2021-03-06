package org.mlink.iwm.util;

import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;
import org.mlink.iwm.util.CopyUtils;

/**
 * updated version of apache's LongConverted
 * this one tolerates nulls
 */
public class LongConverter implements Converter {
    private static final Logger logger = Logger.getLogger(LongConverter.class);

    /**
     * Create a {@link Converter} that will throw a {@link org.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     */
    public LongConverter() {
        logger.debug("registering " + this.getClass());
    }

    // --------------------------------------------------------- Public Methods


    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param type  Data type to which this value should be converted
     * @param value The input value to be converted
     * @throws org.apache.commons.beanutils.ConversionException
     *          if conversion cannot be performed
     *          successfully
     */


    public Object convert(Class type, Object value) {
        Object rtn;
        if (value == null) {
            rtn = null;
        } else if (value instanceof Long) {
            rtn =  (value);
        } else if (value instanceof Number) {
            rtn =  ((Number) value).longValue();
        } else if (value instanceof String) {
            String str = ((String)value).trim();
            if(str.length()==0){
                rtn = CopyUtils.isBaseVOTarget()?CopyUtils.NullAliasValues.get(CopyUtils.NullAlias.Long):null;
            }else{
                rtn = new Long(value.toString());
            }
        } else {
            rtn =  new Long(value.toString());
        }
        return rtn;
    }
}
