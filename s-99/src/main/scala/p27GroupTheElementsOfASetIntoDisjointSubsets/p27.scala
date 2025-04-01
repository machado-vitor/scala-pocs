package p27GroupTheElementsOfASetIntoDisjointSubsets

import p26GenerateTheCombinationOfKDistinctObjectsChosenFromTheNElementsOfAList.combinations

//scala> group3(List("Aldo", "Beat", "Carla", "David", "Evi", "Flip", "Gary", "Hugo", "Ida"))
//res0: List[List[List[String]]] = List(List(List(Aldo, Beat), List(Carla, David, Evi), List(Flip, Gary, Hugo, Ida)), ...

@main
def main(): Unit = {
  println("input: List(\"Aldo\", \"Beat\", \"Carla\", \"David\", \"Evi\", \"Flip\", \"Gary\", \"Hugo\", \"Ida\")")
  println(s"output: ${group3(List("Aldo", "Beat", "Carla", "David", "Evi", "Flip", "Gary", "Hugo", "Ida"))}")
}

def group3[A](list: List[A]): List[List[List[A]]] =
  for
    group1 <- combinations(2, list)
    remainingAfter2 = list.diff(group1)
    group2 <- combinations(3, remainingAfter2)
    group3 = remainingAfter2.diff(group2)
  yield List(group1, group2, group3)
