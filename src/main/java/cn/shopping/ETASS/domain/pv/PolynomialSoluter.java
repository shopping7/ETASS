package cn.shopping.ETASS.domain.pv;

import it.unisa.dia.gas.jpbc.Element;

public class PolynomialSoluter {
    private Element[][] matrix;
    private Element[] result;
    private int order;

    public PolynomialSoluter() {
    }

    // 检查输入项长度
    private boolean init(Element[][] matrixA, Element[] arrayB) {
        order = arrayB.length;
        if (matrixA.length != order)
            return false;
        for (Element[] arrayA : matrixA)
            if (arrayA.length != order)
                return false;
        matrix = matrixA;
        result = arrayB;
        return true;
    }

    public Element[] getResult(Element[][] matrixA, Element[] arrayB) {
        if (!init(matrixA, arrayB))
            return null;

        // 高斯消元-正向
        for (int i = 0; i < order; i++) {
            // 如果当前行对角线项为0则与后面的同列项非0的行交换
            if (!swithIfZero(i))
                return null;
            // 消元：向下所有行的当前位消为0
            for (int j = i + 1; j < order; j++) {
                if (matrix[j][i].isZero())
                    continue;
                Element factor = matrix[j][i].div(matrix[i][i]).getImmutable();
                for (int l = i; l < order; l++)
                    matrix[j][l] = matrix[j][l].sub(matrix[i][l].mul(factor));
                // 不生成增广矩阵，因此同步计算结果向量
                result[j] = result[j].sub(result[i].mul(factor));
            }
        }

        // 高斯消元-反向-去掉了冗余计算：逆向求结果
        for (int i = order - 1; i >= 0; i--) {
            result[i] = result[i].div(matrix[i][i]).getImmutable();
            for (int j = i - 1; j > -1; j--)
                result[j] = result[j].sub(result[i].mul(matrix[j][i]));
        }
        return result.clone();
    }

    // 如果第i位为0，则往下找第i位非零的行，与其对调
    private boolean swithIfZero(int i) {
        if (matrix[i][i].isZero()) {
            int j = i + 1;
            // 找到对应位置非0的列
            while (j < order && matrix[j][i].isZero())
                j++;
            // 若对应位置全为0则无解
            if (j == order)
                return false;
            else
                switchRows(i, j);
        }
        return true;
    }

    // 调换行
    private void switchRows(int i, int j) {
        Element[] tmp1 = matrix[i];
        matrix[i] = matrix[j];
        matrix[j] = tmp1;
        Element tmp2 = result[i];
        result[i] = result[j];
        result[j] = tmp2;
    }
}
