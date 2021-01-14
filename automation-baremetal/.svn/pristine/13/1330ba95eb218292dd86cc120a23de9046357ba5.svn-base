package com.oracle.opc.automation.test.entity.enums;

import com.oracle.opc.automation.entity.enums.CloudServiceType;

public enum Shapes {
	
		HighIO("BM.HighIO1.36", CloudServiceType.BMDBAC),
		DenseIO("BM.DenseIO1.36", CloudServiceType.BMDBAC),
		Quarter("Exadata.Quarter1.84", CloudServiceType.BMEXAAC),
		Half("Exadata.Half1.168", CloudServiceType.BMEXAAC),
		Full("Exadata.Full1.336", CloudServiceType.BMEXAAC),
		RAC("BM.RACLocalStorage1.72", CloudServiceType.BMDBAC);

		private String shapeName;
		private CloudServiceType type;

		
		Shapes(String shapeName, CloudServiceType type) {
			this.setShapeName(shapeName);
			this.setType(type);
		}
		
		public String getShapeName() {
			return shapeName;
		}

		public void setShapeName(String shapeName) {
			this.shapeName = shapeName;
		}

		public static Shapes getShape(String t) {
			for (Shapes type : Shapes.values()) {
				if (t.equalsIgnoreCase(type.name())) {
					return type;
				}
			}
			return null;
		}

		public CloudServiceType getType() {
			return type;
		}

		public void setType(CloudServiceType type) {
			this.type = type;
		}

}
