package util;

public class Validation {

    /**
     * Mengecek apakah input kosong.
     *
     * @param text Input yang akan dicek
     * @return true jika kosong
     */
    public static boolean isEmpty(String text) {

        return text == null || text.trim().isEmpty();

    }

    /**
     * Mengecek apakah input berupa angka.
     *
     * @param text Input
     * @return true jika angka
     */
    public static boolean isNumeric(String text) {

        if (isEmpty(text)) {

            return false;

        }

        try {

            Double.parseDouble(text);

            return true;

        } catch (NumberFormatException e) {

            return false;

        }

    }

    /**
     * Mengecek apakah input berupa integer.
     */
    public static boolean isInteger(String text) {

        if (isEmpty(text)) {

            return false;

        }

        try {

            Integer.parseInt(text);

            return true;

        } catch (NumberFormatException e) {

            return false;

        }

    }
        /**
     * Mengecek apakah input hanya terdiri dari huruf dan spasi.
     *
     * @param text Input
     * @return true jika hanya huruf
     */
    public static boolean isAlphabet(String text) {

        if (isEmpty(text)) {

            return false;

        }

        return text.matches("[a-zA-Z\\s]+");

    }

    /**
     * Mengecek apakah nomor HP valid.
     *
     * Hanya menerima angka dengan panjang 10-15 digit.
     *
     * @param phone Nomor HP
     * @return true jika valid
     */
    public static boolean isPhoneNumber(String phone) {

        if (isEmpty(phone)) {

            return false;

        }

        return phone.matches("\\d{10,15}");

    }

    /**
     * Mengecek panjang minimal karakter.
     *
     * @param text Input
     * @param length Panjang minimal
     * @return true jika memenuhi
     */
    public static boolean hasMinLength(String text, int length) {

        if (isEmpty(text)) {

            return false;

        }

        return text.trim().length() >= length;

    }

    /**
     * Mengecek apakah Serial Number valid.
     *
     * Minimal 5 karakter.
     *
     * @param serialNumber Serial Number
     * @return true jika valid
     */
    public static boolean isValidSerialNumber(String serialNumber) {

        return hasMinLength(serialNumber, 5);

    }
        /**
     * Mengecek apakah angka bernilai positif.
     *
     * @param text Input angka
     * @return true jika lebih dari 0
     */
    public static boolean isPositiveNumber(String text) {

        if (!isNumeric(text)) {

            return false;

        }

        return Double.parseDouble(text) > 0;

    }

    /**
     * Mengecek apakah status servis valid.
     *
     * Status yang diperbolehkan:
     * - Menunggu
     * - Diproses
     * - Selesai
     *
     * @param status Status servis
     * @return true jika valid
     */
    public static boolean isValidStatus(String status) {

        if (isEmpty(status)) {

            return false;

        }

        return status.equalsIgnoreCase("Menunggu")
                || status.equalsIgnoreCase("Diproses")
                || status.equalsIgnoreCase("Selesai");

    }

    /**
     * Mengecek apakah biaya servis valid.
     *
     * @param biaya Biaya servis
     * @return true jika berupa angka positif
     */
    public static boolean isValidCost(String biaya) {

        return isPositiveNumber(biaya);

    }

}