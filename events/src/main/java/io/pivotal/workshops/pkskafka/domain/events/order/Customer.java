/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package io.pivotal.workshops.pkskafka.domain.events.order;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Customer extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -2830431323762477401L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Customer\",\"namespace\":\"io.pivotal.workshops.pkskafka.domain.events.order\",\"fields\":[{\"name\":\"firstName\",\"type\":\"string\",\"doc\":\"Customer's first name\"},{\"name\":\"lastName\",\"type\":\"string\",\"doc\":\"Customer's last name\"},{\"name\":\"automatedEmail\",\"type\":\"boolean\",\"doc\":\"True if customer signed up for receiving promotional emails\",\"default\":true},{\"name\":\"customerEmails\",\"type\":{\"type\":\"array\",\"items\":\"string\"},\"doc\":\"Customer Email Addresses\",\"default\":[]},{\"name\":\"customerAddress\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"CustomerAddr\",\"fields\":[{\"name\":\"address\",\"type\":\"string\",\"doc\":\"Address Line 1\"},{\"name\":\"city\",\"type\":\"string\"},{\"name\":\"state\",\"type\":\"string\"},{\"name\":\"zipcode\",\"type\":[\"string\",\"int\"]}]}},\"doc\":\"Complex Type - Refer Customer Address\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Customer> ENCODER =
      new BinaryMessageEncoder<Customer>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Customer> DECODER =
      new BinaryMessageDecoder<Customer>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<Customer> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<Customer> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Customer>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this Customer to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a Customer from a ByteBuffer. */
  public static Customer fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Customer's first name */
  @Deprecated public java.lang.CharSequence firstName;
  /** Customer's last name */
  @Deprecated public java.lang.CharSequence lastName;
  /** True if customer signed up for receiving promotional emails */
  @Deprecated public boolean automatedEmail;
  /** Customer Email Addresses */
  @Deprecated public java.util.List<java.lang.CharSequence> customerEmails;
  /** Complex Type - Refer Customer Address */
  @Deprecated public java.util.List<io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr> customerAddress;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Customer() {}

  /**
   * All-args constructor.
   * @param firstName Customer's first name
   * @param lastName Customer's last name
   * @param automatedEmail True if customer signed up for receiving promotional emails
   * @param customerEmails Customer Email Addresses
   * @param customerAddress Complex Type - Refer Customer Address
   */
  public Customer(java.lang.CharSequence firstName, java.lang.CharSequence lastName, java.lang.Boolean automatedEmail, java.util.List<java.lang.CharSequence> customerEmails, java.util.List<io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr> customerAddress) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.automatedEmail = automatedEmail;
    this.customerEmails = customerEmails;
    this.customerAddress = customerAddress;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return firstName;
    case 1: return lastName;
    case 2: return automatedEmail;
    case 3: return customerEmails;
    case 4: return customerAddress;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: firstName = (java.lang.CharSequence)value$; break;
    case 1: lastName = (java.lang.CharSequence)value$; break;
    case 2: automatedEmail = (java.lang.Boolean)value$; break;
    case 3: customerEmails = (java.util.List<java.lang.CharSequence>)value$; break;
    case 4: customerAddress = (java.util.List<io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'firstName' field.
   * @return Customer's first name
   */
  public java.lang.CharSequence getFirstName() {
    return firstName;
  }

  /**
   * Sets the value of the 'firstName' field.
   * Customer's first name
   * @param value the value to set.
   */
  public void setFirstName(java.lang.CharSequence value) {
    this.firstName = value;
  }

  /**
   * Gets the value of the 'lastName' field.
   * @return Customer's last name
   */
  public java.lang.CharSequence getLastName() {
    return lastName;
  }

  /**
   * Sets the value of the 'lastName' field.
   * Customer's last name
   * @param value the value to set.
   */
  public void setLastName(java.lang.CharSequence value) {
    this.lastName = value;
  }

  /**
   * Gets the value of the 'automatedEmail' field.
   * @return True if customer signed up for receiving promotional emails
   */
  public java.lang.Boolean getAutomatedEmail() {
    return automatedEmail;
  }

  /**
   * Sets the value of the 'automatedEmail' field.
   * True if customer signed up for receiving promotional emails
   * @param value the value to set.
   */
  public void setAutomatedEmail(java.lang.Boolean value) {
    this.automatedEmail = value;
  }

  /**
   * Gets the value of the 'customerEmails' field.
   * @return Customer Email Addresses
   */
  public java.util.List<java.lang.CharSequence> getCustomerEmails() {
    return customerEmails;
  }

  /**
   * Sets the value of the 'customerEmails' field.
   * Customer Email Addresses
   * @param value the value to set.
   */
  public void setCustomerEmails(java.util.List<java.lang.CharSequence> value) {
    this.customerEmails = value;
  }

  /**
   * Gets the value of the 'customerAddress' field.
   * @return Complex Type - Refer Customer Address
   */
  public java.util.List<io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr> getCustomerAddress() {
    return customerAddress;
  }

  /**
   * Sets the value of the 'customerAddress' field.
   * Complex Type - Refer Customer Address
   * @param value the value to set.
   */
  public void setCustomerAddress(java.util.List<io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr> value) {
    this.customerAddress = value;
  }

  /**
   * Creates a new Customer RecordBuilder.
   * @return A new Customer RecordBuilder
   */
  public static io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder newBuilder() {
    return new io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder();
  }

  /**
   * Creates a new Customer RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Customer RecordBuilder
   */
  public static io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder newBuilder(io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder other) {
    return new io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder(other);
  }

  /**
   * Creates a new Customer RecordBuilder by copying an existing Customer instance.
   * @param other The existing instance to copy.
   * @return A new Customer RecordBuilder
   */
  public static io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder newBuilder(io.pivotal.workshops.pkskafka.domain.events.order.Customer other) {
    return new io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder(other);
  }

  /**
   * RecordBuilder for Customer instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Customer>
    implements org.apache.avro.data.RecordBuilder<Customer> {

    /** Customer's first name */
    private java.lang.CharSequence firstName;
    /** Customer's last name */
    private java.lang.CharSequence lastName;
    /** True if customer signed up for receiving promotional emails */
    private boolean automatedEmail;
    /** Customer Email Addresses */
    private java.util.List<java.lang.CharSequence> customerEmails;
    /** Complex Type - Refer Customer Address */
    private java.util.List<io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr> customerAddress;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.firstName)) {
        this.firstName = data().deepCopy(fields()[0].schema(), other.firstName);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.lastName)) {
        this.lastName = data().deepCopy(fields()[1].schema(), other.lastName);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.automatedEmail)) {
        this.automatedEmail = data().deepCopy(fields()[2].schema(), other.automatedEmail);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.customerEmails)) {
        this.customerEmails = data().deepCopy(fields()[3].schema(), other.customerEmails);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.customerAddress)) {
        this.customerAddress = data().deepCopy(fields()[4].schema(), other.customerAddress);
        fieldSetFlags()[4] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing Customer instance
     * @param other The existing instance to copy.
     */
    private Builder(io.pivotal.workshops.pkskafka.domain.events.order.Customer other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.firstName)) {
        this.firstName = data().deepCopy(fields()[0].schema(), other.firstName);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.lastName)) {
        this.lastName = data().deepCopy(fields()[1].schema(), other.lastName);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.automatedEmail)) {
        this.automatedEmail = data().deepCopy(fields()[2].schema(), other.automatedEmail);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.customerEmails)) {
        this.customerEmails = data().deepCopy(fields()[3].schema(), other.customerEmails);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.customerAddress)) {
        this.customerAddress = data().deepCopy(fields()[4].schema(), other.customerAddress);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'firstName' field.
      * Customer's first name
      * @return The value.
      */
    public java.lang.CharSequence getFirstName() {
      return firstName;
    }

    /**
      * Sets the value of the 'firstName' field.
      * Customer's first name
      * @param value The value of 'firstName'.
      * @return This builder.
      */
    public io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder setFirstName(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.firstName = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'firstName' field has been set.
      * Customer's first name
      * @return True if the 'firstName' field has been set, false otherwise.
      */
    public boolean hasFirstName() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'firstName' field.
      * Customer's first name
      * @return This builder.
      */
    public io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder clearFirstName() {
      firstName = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'lastName' field.
      * Customer's last name
      * @return The value.
      */
    public java.lang.CharSequence getLastName() {
      return lastName;
    }

    /**
      * Sets the value of the 'lastName' field.
      * Customer's last name
      * @param value The value of 'lastName'.
      * @return This builder.
      */
    public io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder setLastName(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.lastName = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'lastName' field has been set.
      * Customer's last name
      * @return True if the 'lastName' field has been set, false otherwise.
      */
    public boolean hasLastName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'lastName' field.
      * Customer's last name
      * @return This builder.
      */
    public io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder clearLastName() {
      lastName = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'automatedEmail' field.
      * True if customer signed up for receiving promotional emails
      * @return The value.
      */
    public java.lang.Boolean getAutomatedEmail() {
      return automatedEmail;
    }

    /**
      * Sets the value of the 'automatedEmail' field.
      * True if customer signed up for receiving promotional emails
      * @param value The value of 'automatedEmail'.
      * @return This builder.
      */
    public io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder setAutomatedEmail(boolean value) {
      validate(fields()[2], value);
      this.automatedEmail = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'automatedEmail' field has been set.
      * True if customer signed up for receiving promotional emails
      * @return True if the 'automatedEmail' field has been set, false otherwise.
      */
    public boolean hasAutomatedEmail() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'automatedEmail' field.
      * True if customer signed up for receiving promotional emails
      * @return This builder.
      */
    public io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder clearAutomatedEmail() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'customerEmails' field.
      * Customer Email Addresses
      * @return The value.
      */
    public java.util.List<java.lang.CharSequence> getCustomerEmails() {
      return customerEmails;
    }

    /**
      * Sets the value of the 'customerEmails' field.
      * Customer Email Addresses
      * @param value The value of 'customerEmails'.
      * @return This builder.
      */
    public io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder setCustomerEmails(java.util.List<java.lang.CharSequence> value) {
      validate(fields()[3], value);
      this.customerEmails = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'customerEmails' field has been set.
      * Customer Email Addresses
      * @return True if the 'customerEmails' field has been set, false otherwise.
      */
    public boolean hasCustomerEmails() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'customerEmails' field.
      * Customer Email Addresses
      * @return This builder.
      */
    public io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder clearCustomerEmails() {
      customerEmails = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'customerAddress' field.
      * Complex Type - Refer Customer Address
      * @return The value.
      */
    public java.util.List<io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr> getCustomerAddress() {
      return customerAddress;
    }

    /**
      * Sets the value of the 'customerAddress' field.
      * Complex Type - Refer Customer Address
      * @param value The value of 'customerAddress'.
      * @return This builder.
      */
    public io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder setCustomerAddress(java.util.List<io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr> value) {
      validate(fields()[4], value);
      this.customerAddress = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'customerAddress' field has been set.
      * Complex Type - Refer Customer Address
      * @return True if the 'customerAddress' field has been set, false otherwise.
      */
    public boolean hasCustomerAddress() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'customerAddress' field.
      * Complex Type - Refer Customer Address
      * @return This builder.
      */
    public io.pivotal.workshops.pkskafka.domain.events.order.Customer.Builder clearCustomerAddress() {
      customerAddress = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Customer build() {
      try {
        Customer record = new Customer();
        record.firstName = fieldSetFlags()[0] ? this.firstName : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.lastName = fieldSetFlags()[1] ? this.lastName : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.automatedEmail = fieldSetFlags()[2] ? this.automatedEmail : (java.lang.Boolean) defaultValue(fields()[2]);
        record.customerEmails = fieldSetFlags()[3] ? this.customerEmails : (java.util.List<java.lang.CharSequence>) defaultValue(fields()[3]);
        record.customerAddress = fieldSetFlags()[4] ? this.customerAddress : (java.util.List<io.pivotal.workshops.pkskafka.domain.events.order.CustomerAddr>) defaultValue(fields()[4]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Customer>
    WRITER$ = (org.apache.avro.io.DatumWriter<Customer>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Customer>
    READER$ = (org.apache.avro.io.DatumReader<Customer>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}