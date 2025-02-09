class Node {
    constructor(value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}

class AVLTree {
    constructor() {
        this.root = null;
    }

    getHeight(node) {
        return node ? node.height : 0;
    }

    updateHeight(node) {
        node.height = Math.max(this.getHeight(node.left), this.getHeight(node.right)) + 1;
    }

    getBalanceFactor(node) {
        return this.getHeight(node.left) - this.getHeight(node.right);
    }

    rightRotate(y) {
        const x = y.left;
        const T2 = x.right;

        x.right = y;
        y.left = T2;

        this.updateHeight(y);
        this.updateHeight(x);

        return x;
    }

    leftRotate(x) {
        const y = x.right;
        const T2 = y.left;

        y.left = x;
        x.right = T2;

        this.updateHeight(x);
        this.updateHeight(y);

        return y;
    }

    insert(value) {
        this.root = this._insert(this.root, value);
    }

    _insert(node, value) {
        if (!node) return new Node(value);

        if (value < node.value) {
            node.left = this._insert(node.left, value);
        } else if (value > node.value) {
            node.right = this._insert(node.right, value);
        } else {
            return node;
        }

        this.updateHeight(node);
        const balance = this.getBalanceFactor(node);

        if (balance > 1 && value < node.left.value) return this.rightRotate(node);
        if (balance < -1 && value > node.right.value) return this.leftRotate(node);
        if (balance > 1 && value > node.left.value) {
            node.left = this.leftRotate(node.left);
            return this.rightRotate(node);
        }
        if (balance < -1 && value < node.right.value) {
            node.right = this.rightRotate(node.right);
            return this.leftRotate(node);
        }

        return node;
    }

    delete(value) {
        this.root = this._delete(this.root, value);
    }

    _delete(node, value) {
        if (!node) return null;

        if (value < node.value) {
            node.left = this._delete(node.left, value);
        } else if (value > node.value) {
            node.right = this._delete(node.right, value);
        } else {
            if (!node.left) return node.right;
            if (!node.right) return node.left;

            const temp = this._findMin(node.right);
            node.value = temp.value;
            node.right = this._delete(node.right, temp.value);
        }

        this.updateHeight(node);
        const balance = this.getBalanceFactor(node);

        if (balance > 1 && this.getBalanceFactor(node.left) >= 0) return this.rightRotate(node);
        if (balance < -1 && this.getBalanceFactor(node.right) <= 0) return this.leftRotate(node);
        if (balance > 1 && this.getBalanceFactor(node.left) < 0) {
            node.left = this.leftRotate(node.left);
            return this.rightRotate(node);
        }
        if (balance < -1 && this.getBalanceFactor(node.right) > 0) {
            node.right = this.rightRotate(node.right);
            return this.leftRotate(node);
        }

        return node;
    }

    _findMin(node) {
        while (node.left) node = node.left;
        return node;
    }

    search(value) {
        return this._search(this.root, value);
    }
    
    _search(node, value) {
        if (!node) return false; // Not found
        if (value === node.value) return true; // Found!
        if (value < node.value) return this._search(node.left, value); // Search left
        return this._search(node.right, value); // Search right
    }
    
}

// D3.js visualization
function updateVisualization() {
    d3.select("svg").selectAll("*").remove();
    drawTree(avlTree.root, 400, 50, 200);
}

function drawTree(node, x, y, offset) {
    if (!node) return;

    if (node.left) {
        d3.select("svg")
            .append("line")
            .attr("x1", x)
            .attr("y1", y)
            .attr("x2", x - offset)
            .attr("y2", y + 80)
            .attr("stroke", "black");
        drawTree(node.left, x - offset, y + 80, offset / 2);
    }

    if (node.right) {
        d3.select("svg")
            .append("line")
            .attr("x1", x)
            .attr("y1", y)
            .attr("x2", x + offset)
            .attr("y2", y + 80)
            .attr("stroke", "black");
        drawTree(node.right, x + offset, y + 80, offset / 2);
    }

    d3.select("svg")
        .append("circle")
        .attr("cx", x)
        .attr("cy", y)
        .attr("r", 20)
        .attr("fill", "lightblue")
        .attr("stroke", "black");

    d3.select("svg")
        .append("text")
        .attr("x", x)
        .attr("y", y + 5)
        .attr("text-anchor", "middle")
        .attr("font-size", "16px")
        .text(node.value);
}

const avlTree = new AVLTree();

function insertNode() {
    const value = parseInt(document.getElementById("nodeValue").value);
    if (!isNaN(value)) {
        avlTree.insert(value);
        updateVisualization();
    }
}

function deleteNode() {
    const value = parseInt(document.getElementById("nodeValue").value);
    if (!isNaN(value)) {
        avlTree.delete(value);
        updateVisualization();
    }
}

function searchNode() {
    const value = parseInt(document.getElementById("nodeValue").value);
    if (!isNaN(value)) {
        const found = avlTree.search(value);
        alert(found ? "Node found!" : "Node not found!");
    }
}
