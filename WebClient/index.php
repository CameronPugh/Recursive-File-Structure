<!DOCTYPE HTML>  
<html>
<head>
</head>
<body>  

<?php
$term = "";
$names = [];

if ($_SERVER["REQUEST_METHOD"] == "POST") {
  $term = test_input($_POST["term"]);
  $names = getData($term);
}

function getData($term){
  $db = new SQLite3('/Users/cameronpugh/SFile.db');
  $names = [];

  $results = $db->query("SELECT * FROM path WHERE name LIKE '%$term%' ;");
  
  while ($row = $results->fetchArray()) {
      $names[] = [
          'name' => $row['name'],
      ];
  }
  return $names;
}

function test_input($data) {
  $data = trim($data);
  $data = htmlspecialchars($data);
  return $data;
}
?>

<h2>PHP - Search File Structure</h2>
<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">  
  Search Term: <input type="text" name="term">
  <br><br>
  <input type="submit" name="submit" value="Submit">  
</form>


<?php 

echo "<h2>Found Paths:</h2>";

if (count($names) > 0): ?>
  <table>
    <tbody>
  <?php foreach ($names as $item): array_map('htmlentities', $item); ?>
      <tr>
        <td><?php echo implode('</td><td>',  $item); ?></td>
      </tr>
  <?php endforeach; ?>
    </tbody>
  </table>
  <?php endif; ?>

</body>
</html>