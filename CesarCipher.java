class CesarCipher {

	public CesarCipher() {
	}

	public CesarCipher(String s) {
		String s1 = encryptCesar(s.toLowerCase());
		String s2 = decryptCesar(s1);

		System.out.println("Encrypted: " + s1);
		System.out.println("Decrypted: " + s2);
	}

	public String encryptCesar(String s) {
		char[] c = s.toCharArray();
		char[] afterEncryption = new char[c.length];
		int[] tmp = new int[c.length];
		int[] tmp2 = new int[c.length];

		for (int i = 0; i < c.length; i++) {
			tmp[i] = (int) c[i];

			if (tmp[i] < 97 || tmp[i] > 122)
				tmp2[i] = tmp[i];
			else if (tmp[i] > 96 && tmp[i] < 120)
				tmp2[i] = tmp[i] + 3;
			else {
				if (tmp[i] == 122)
					tmp2[i] = 97;
				if (tmp[i] == 121)
					tmp2[i] = 98;
				if (tmp[i] == 120)
					tmp2[i] = 99;
			}

			afterEncryption[i] = (char) tmp2[i];
		}

		return String.valueOf(afterEncryption);
	}

	public String decryptCesar(String s) {
		char[] c = s.toCharArray();
		char[] beforeEncryption = new char[c.length];
		int[] tmp = new int[c.length];
		int[] tmp2 = new int[c.length];

		for (int i = 0; i < c.length; i++) {
			tmp[i] = (int) c[i];

			if (tmp[i] < 97 || tmp[i] > 122)
				tmp2[i] = tmp[i];
			else if (tmp[i] > 99 && tmp[i] < 123)
				tmp2[i] = tmp[i] - 3;
			else {
				if (tmp[i] == 97)
					tmp2[i] = 122;
				if (tmp[i] == 98)
					tmp2[i] = 121;
				if (tmp[i] == 99)
					tmp2[i] = 120;
			}

			beforeEncryption[i] = (char) tmp2[i];
		}

		return String.valueOf(beforeEncryption);
	}
}
